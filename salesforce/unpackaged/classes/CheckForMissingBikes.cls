global class CheckForMissingBikes implements Schedulable {

    global void execute(SchedulableContext SC) {
        List<Reservation__c> reservationsThatNeedCases = [
            SELECT Id, Contact__c
            FROM Reservation__c
            WHERE Id NOT IN (SELECT Reservation__c FROM Case) AND Id IN :reservationsWithMissingBikes()
        ];
        
        for (Reservation__c r : reservationsThatNeedCases) {
            Case c = new Case(
                Reservation__c = r.Id,
                ContactId = r.Contact__c,
                Subject = 'A Rental Bike is Missing!'
            );
            insert c;
        }
    }
    
    // Bikes with active reservations that have not received a ping for 30 minutes
    public static List<Reservation__c> reservationsWithMissingBikes() {
        DateTime timeSince = System.now().addMinutes(-30);
        
        // this does not include reservations that have been active for less than 30 minutes
        List<Reservation__c> reservationsWithMissingBikes = [
            SELECT Id, Rental_Bike__c
            FROM Reservation__c
            WHERE Start__c < :timeSince AND
            	  End__c > :System.now() AND
            	  (Rental_Bike__r.Ping_Moment__c < :timeSince OR Rental_Bike__r.Ping_Moment__c = NULL)
        ];
       
        return reservationsWithMissingBikes;
    }
    
}