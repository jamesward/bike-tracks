@istest
class CheckForMissingBikesTest {

    @istest
    static void thereShouldBeOneMissingBikeWhenThereIsNoPingData() {
        Rental_Bike__c b = new Rental_Bike__c(Name = 'test bike');
        insert b;
        
        Contact c = new Contact(LastName = 'Bar');
        insert c;
        
        Reservation__c r = new Reservation__c(
            Contact__c = c.Id,
            Rental_Bike__c = b.Id,
            Start__c = System.now().addMinutes(-60),
            End__c = System.now().addMinutes(60)
        );
        insert r;
        
        System.assert(CheckForMissingBikes.reservationsWithMissingBikes().size() == 1);
    }
    
    @istest
    static void thereShouldBeOneMissingBikeWithAnOldPing() {
        Rental_Bike__c b = new Rental_Bike__c(
            Name = 'test bike',
            Ping_Moment__c = System.now().addMinutes(-45)
        );
        insert b;
        
        Contact c = new Contact(LastName = 'Bar');
        insert c;
        
        Reservation__c r = new Reservation__c(
            Contact__c = c.Id,
            Rental_Bike__c = b.Id,
            Start__c = System.now().addMinutes(-60),
            End__c = System.now().addMinutes(60)
        );
        insert r;
        
        System.assert(CheckForMissingBikes.reservationsWithMissingBikes().size() == 1);
    }
    
    @istest
    static void thereShouldBeNoMissingBikes() {
        Rental_Bike__c b = new Rental_Bike__c(
            Name = 'test bike',
            Ping_Moment__c = System.now().addMinutes(-15)
        );
        insert b;
        
        Contact c = new Contact(LastName = 'Bar');
        insert c;
        
        Reservation__c r = new Reservation__c(
            Contact__c = c.Id,
            Rental_Bike__c = b.Id,
            Start__c = System.now().addMinutes(-60),
            End__c = System.now().addMinutes(60)
        );
        insert r;
        
        System.assert(CheckForMissingBikes.reservationsWithMissingBikes().size() == 0);
    }
    
    @istest
    static void thereShouldBeNoMissingBikesWithARecentlyStartedReservation() {
        Rental_Bike__c b = new Rental_Bike__c(Name = 'test bike');
        insert b;
        
        Contact c = new Contact(LastName = 'Bar');
        insert c;
        
        Reservation__c r = new Reservation__c(
            Contact__c = c.Id,
            Rental_Bike__c = b.Id,
            Start__c = System.now().addMinutes(-10),
            End__c = System.now().addMinutes(60)
        );
        insert r;
        
        System.assert(CheckForMissingBikes.reservationsWithMissingBikes().size() == 0);
    }
    
    @istest
    static void casesShouldBeCreatedForMissingBikes() {
        Rental_Bike__c b = new Rental_Bike__c(Name = 'test bike');
        insert b;
        
        Contact c = new Contact(LastName = 'Bar');
        insert c;
        
        Reservation__c r = new Reservation__c(
            Contact__c = c.Id,
            Rental_Bike__c = b.Id,
            Start__c = System.now().addMinutes(-60),
            End__c = System.now().addMinutes(60)
        );
        insert r;
        
        Test.startTest();
        
        String jobId = System.schedule(
            'casesShouldBeCreatedForMissingBikes',
            '0 0 * * * ?',
            new CheckForMissingBikes()
        );

        Test.stopTest();
        
        Integer numCases = [
            SELECT count()
            FROM Case
            WHERE Reservation__c = :r.Id
        ];
        
        System.assert(numCases == 1);
    }
    
    @istest
    static void casesShouldNotBeCreatedForReservationsThatAlreadyHaveOne() {
        Rental_Bike__c b = new Rental_Bike__c(Name = 'test bike');
        insert b;
        
        Contact c = new Contact(LastName = 'Bar');
        insert c;
        
        Reservation__c r = new Reservation__c(
            Contact__c = c.Id,
            Rental_Bike__c = b.Id,
            Start__c = System.now().addMinutes(-60),
            End__c = System.now().addMinutes(60)
        );
        insert r;
        
        Case theCase = new Case(
            Reservation__c = r.Id,
            ContactId = c.Id
        );
        insert theCase;
        
        Test.startTest();
        
        String jobId = System.schedule(
            'casesShouldNotBeCreatedForReservationsThatAlreadyHaveOne',
            '0 0 * * * ?',
            new CheckForMissingBikes()
        );

        Test.stopTest();
        
        Integer numCases = [
            SELECT count()
            FROM Case
            WHERE Reservation__c = :r.Id
        ];
        
        System.assert(numCases == 1);
    }
    
}