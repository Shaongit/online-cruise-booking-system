package com.cruise.booking.config;

import com.cruise.booking.entity.*;
import com.cruise.booking.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    private final PortRepository portRepository;
    private final ShipRepository shipRepository;
    private final CabinTypeRepository cabinTypeRepository;
    private final UserRepository userRepository;
    private final CruiseRepository cruiseRepository;
    private final CruiseCabinRepository cruiseCabinRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(PortRepository portRepository,
                      ShipRepository shipRepository,
                      CabinTypeRepository cabinTypeRepository,
                      UserRepository userRepository,
                      CruiseRepository cruiseRepository,
                      CruiseCabinRepository cruiseCabinRepository,
                      PasswordEncoder passwordEncoder) {
        this.portRepository = portRepository;
        this.shipRepository = shipRepository;
        this.cabinTypeRepository = cabinTypeRepository;
        this.userRepository = userRepository;
        this.cruiseRepository = cruiseRepository;
        this.cruiseCabinRepository = cruiseCabinRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // ---------------------------------------------------------------
        // Migrate existing users: hash any plain-text passwords and set role
        // ---------------------------------------------------------------
        userRepository.findAll().forEach(u -> {
            boolean changed = false;
            if (u.getPasswordHash() != null && !u.getPasswordHash().startsWith("$2")) {
                u.setPasswordHash(passwordEncoder.encode(u.getPasswordHash()));
                changed = true;
            }
            if (u.getRole() == null || u.getRole().isBlank()) {
                u.setRole("ROLE_USER");
                changed = true;
            }
            if (changed) userRepository.save(u);
        });

        // Ensure an admin account always exists
        if (!userRepository.existsByEmail("admin@cruise.com")) {
            User admin = new User();
            admin.setEmail("admin@cruise.com");
            admin.setPasswordHash(passwordEncoder.encode("Admin@123"));
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole("ROLE_ADMIN");
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            userRepository.save(admin);
            System.out.println("Admin account created: admin@cruise.com / Admin@123");
        }

        // Check if data already exists to avoid duplicate entries
        if (portRepository.count() > 0) {
            System.out.println("Database already contains data. Skipping seed data loading.");
            return;
        }

        System.out.println("Loading seed data into database...");

        // Load Ports
        Port portMiami = createPort("Port of Miami", "Miami", "USA", "MIA");
        Port portNassau = createPort("Nassau Cruise Port", "Nassau", "Bahamas", "NAS");
        Port portCozumel = createPort("Port of Cozumel", "Cozumel", "Mexico", "CZM");
        Port portGrandCayman = createPort("George Town Port", "George Town", "Cayman Islands", "GCM");
        Port portRome = createPort("Port of Civitavecchia", "Rome", "Italy", "ROM");
        Port portBarcelona = createPort("Port of Barcelona", "Barcelona", "Spain", "BCN");
        Port portVenice = createPort("Port of Venice", "Venice", "Italy", "VCE");
        Port portSantorini = createPort("Santorini Port", "Santorini", "Greece", "JTR");
        Port portSeattle = createPort("Port of Seattle", "Seattle", "USA", "SEA");
        Port portJuneau = createPort("Port of Juneau", "Juneau", "USA", "JNU");

        portRepository.save(portMiami);
        portRepository.save(portNassau);
        portRepository.save(portCozumel);
        portRepository.save(portGrandCayman);
        portRepository.save(portRome);
        portRepository.save(portBarcelona);
        portRepository.save(portVenice);
        portRepository.save(portSantorini);
        portRepository.save(portSeattle);
        portRepository.save(portJuneau);

        // Load Ships
        Ship shipOasis = createShip("Oasis of the Seas", "Mega Ship", 6780, 2009, "Royal Caribbean");
        Ship shipHarmony = createShip("Harmony of the Seas", "Mega Ship", 6687, 2016, "Royal Caribbean");
        Ship shipCarnival = createShip("Carnival Vista", "Large Ship", 4000, 2016, "Carnival Cruise Line");
        Ship shipAllure = createShip("Allure of the Seas", "Mega Ship", 6780, 2010, "Royal Caribbean");
        Ship shipNorwegian = createShip("Norwegian Epic", "Large Ship", 4228, 2010, "Norwegian Cruise Line");

        shipRepository.save(shipOasis);
        shipRepository.save(shipHarmony);
        shipRepository.save(shipCarnival);
        shipRepository.save(shipAllure);
        shipRepository.save(shipNorwegian);

        // Load Cabin Types
        CabinType interior = createCabinType("Interior", "Cozy interior cabin without window", 2, 
                                             new BigDecimal("150"), "TV, mini-fridge, safe", "2 Twin or 1 Queen");
        CabinType oceanView = createCabinType("Ocean View", "Cabin with ocean view window", 2, 
                                              new BigDecimal("180"), "TV, mini-fridge, safe, window", "2 Twin or 1 Queen");
        CabinType balcony = createCabinType("Balcony", "Private balcony with ocean view", 2, 
                                            new BigDecimal("220"), "TV, mini-fridge, safe, private balcony", "2 Twin or 1 Queen");
        CabinType suite = createCabinType("Suite", "Luxurious suite with separate living area", 4, 
                                          new BigDecimal("400"), "TV, mini-bar, safe, private balcony, living area, premium amenities", "1 King + sofa bed");
        CabinType family = createCabinType("Family Suite", "Spacious suite for families", 6, 
                                           new BigDecimal("550"), "TV, mini-bar, safe, private balcony, kids area, multiple bedrooms", "2 Queen + bunk beds");

        cabinTypeRepository.save(interior);
        cabinTypeRepository.save(oceanView);
        cabinTypeRepository.save(balcony);
        cabinTypeRepository.save(suite);
        cabinTypeRepository.save(family);

        // Load Users
        User user1 = createUser("john.doe@email.com", "password123", "John", "Doe", 
                               "+1-305-555-0101", "123 Ocean Drive", "Miami", "33139");
        User user2 = createUser("jane.smith@email.com", "password123", "Jane", "Smith", 
                               "+1-305-555-0102", "456 Beach Blvd", "Fort Lauderdale", "33301");
        User user3 = createUser("robert.johnson@email.com", "password123", "Robert", "Johnson", 
                               "+1-305-555-0103", "789 Cruise Lane", "Miami", "33140");
        User user4 = createUser("emily.davis@email.com", "password123", "Emily", "Davis", 
                               "+1-305-555-0104", "321 Harbor Street", "Miami Beach", "33139");
        User user5 = createUser("michael.wilson@email.com", "password123", "Michael", "Wilson", 
                               "+1-305-555-0105", "654 Port Avenue", "Miami", "33142");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        // Load Cruises
        Cruise caribbeanCruise = createCruise("Caribbean Paradise", 
                                              LocalDate.of(2026, 4, 15), 
                                              LocalDate.of(2026, 4, 22), 
                                              7, new BigDecimal("899.00"), 
                                              "Available", shipOasis, portMiami, portNassau);
        
        Cruise mediterraneanCruise = createCruise("Mediterranean Explorer", 
                                                  LocalDate.of(2026, 6, 5), 
                                                  LocalDate.of(2026, 6, 15), 
                                                  10, new BigDecimal("1499.00"), 
                                                  "Available", shipHarmony, portRome, portBarcelona);
        
        Cruise mexicoCruise = createCruise("Mexican Riviera", 
                                           LocalDate.of(2026, 5, 10), 
                                           LocalDate.of(2026, 5, 15), 
                                           5, new BigDecimal("599.00"), 
                                           "Available", shipCarnival, portMiami, portCozumel);
        
        Cruise greekIslands = createCruise("Greek Islands Adventure", 
                                          LocalDate.of(2026, 7, 20), 
                                          LocalDate.of(2026, 7, 30), 
                                          10, new BigDecimal("1799.00"), 
                                          "Available", shipAllure, portVenice, portSantorini);
        
        Cruise alaskaCruise = createCruise("Alaska Glacier Discovery", 
                                          LocalDate.of(2026, 8, 1), 
                                          LocalDate.of(2026, 8, 8), 
                                          7, new BigDecimal("1299.00"), 
                                          "Available", shipNorwegian, portSeattle, portJuneau);

        cruiseRepository.save(caribbeanCruise);
        cruiseRepository.save(mediterraneanCruise);
        cruiseRepository.save(mexicoCruise);
        cruiseRepository.save(greekIslands);
        cruiseRepository.save(alaskaCruise);

        // Load Cruise Cabins
        // Caribbean Paradise Cabins
        createAndSaveCruiseCabins(caribbeanCruise, interior, "A-101", "Deck A", 100, new BigDecimal("1.0"), "Interior");
        createAndSaveCruiseCabins(caribbeanCruise, oceanView, "B-201", "Deck B", 80, new BigDecimal("1.2"), "Ocean");
        createAndSaveCruiseCabins(caribbeanCruise, balcony, "C-301", "Deck C", 60, new BigDecimal("1.5"), "Balcony");
        createAndSaveCruiseCabins(caribbeanCruise, suite, "D-401", "Deck D", 20, new BigDecimal("2.5"), "Panoramic");

        // Mediterranean Explorer Cabins
        createAndSaveCruiseCabins(mediterraneanCruise, interior, "A-102", "Deck A", 80, new BigDecimal("1.0"), "Interior");
        createAndSaveCruiseCabins(mediterraneanCruise, oceanView, "B-202", "Deck B", 70, new BigDecimal("1.2"), "Ocean");
        createAndSaveCruiseCabins(mediterraneanCruise, balcony, "C-302", "Deck C", 50, new BigDecimal("1.5"), "Balcony");
        createAndSaveCruiseCabins(mediterraneanCruise, suite, "D-402", "Deck D", 15, new BigDecimal("2.8"), "Panoramic");

        // Mexican Riviera Cabins
        createAndSaveCruiseCabins(mexicoCruise, interior, "A-103", "Deck A", 60, new BigDecimal("1.0"), "Interior");
        createAndSaveCruiseCabins(mexicoCruise, oceanView, "B-203", "Deck B", 50, new BigDecimal("1.3"), "Ocean");
        createAndSaveCruiseCabins(mexicoCruise, balcony, "C-303", "Deck C", 40, new BigDecimal("1.6"), "Balcony");
        createAndSaveCruiseCabins(mexicoCruise, family, "E-501", "Deck E", 10, new BigDecimal("2.2"), "Ocean");

        // Greek Islands Adventure Cabins
        createAndSaveCruiseCabins(greekIslands, oceanView, "B-204", "Deck B", 60, new BigDecimal("1.2"), "Ocean");
        createAndSaveCruiseCabins(greekIslands, balcony, "C-304", "Deck C", 50, new BigDecimal("1.5"), "Balcony");
        createAndSaveCruiseCabins(greekIslands, suite, "D-403", "Deck D", 20, new BigDecimal("2.6"), "Panoramic");

        // Alaska Glacier Discovery Cabins
        createAndSaveCruiseCabins(alaskaCruise, interior, "A-104", "Deck A", 70, new BigDecimal("1.0"), "Interior");
        createAndSaveCruiseCabins(alaskaCruise, oceanView, "B-205", "Deck B", 60, new BigDecimal("1.2"), "Ocean");
        createAndSaveCruiseCabins(alaskaCruise, balcony, "C-305", "Deck C", 50, new BigDecimal("1.5"), "Balcony");
        createAndSaveCruiseCabins(alaskaCruise, suite, "D-404", "Deck D", 15, new BigDecimal("2.5"), "Panoramic");

        System.out.println("Seed data loaded successfully!");
        System.out.println("- Loaded " + portRepository.count() + " ports");
        System.out.println("- Loaded " + shipRepository.count() + " ships");
        System.out.println("- Loaded " + cabinTypeRepository.count() + " cabin types");
        System.out.println("- Loaded " + userRepository.count() + " users");
        System.out.println("- Loaded " + cruiseRepository.count() + " cruises");
        System.out.println("- Loaded " + cruiseCabinRepository.count() + " cruise cabins");
    }

    private Port createPort(String portName, String city, String country, String portCode) {
        Port port = new Port();
        port.setPortName(portName);
        port.setCity(city);
        port.setCountry(country);
        port.setPortCode(portCode);
        return port;
    }

    private Ship createShip(String shipName, String shipType, Integer capacity, Integer yearBuilt, String operator) {
        Ship ship = new Ship();
        ship.setShipName(shipName);
        ship.setShipType(shipType);
        ship.setCapacity(capacity);
        ship.setYearBuilt(yearBuilt);
        ship.setOperator(operator);
        ship.setCreatedAt(LocalDateTime.now());
        return ship;
    }

    private CabinType createCabinType(String typeName, String description, Integer maxOccupancy, 
                                      BigDecimal sizeSqft, String amenities, String bedConfiguration) {
        CabinType cabinType = new CabinType();
        cabinType.setTypeName(typeName);
        cabinType.setDescription(description);
        cabinType.setMaxOccupancy(maxOccupancy);
        cabinType.setSizeSqft(sizeSqft);
        cabinType.setAmenities(amenities);
        cabinType.setBedConfiguration(bedConfiguration);
        return cabinType;
    }

    private User createUser(String email, String password, String firstName, String lastName, 
                           String phone, String address, String city, String postalCode) {
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAddress(address);
        user.setCity(city);
        user.setPostalCode(postalCode);
        user.setRole("ROLE_USER");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    private Cruise createCruise(String cruiseName, LocalDate departureDate, LocalDate returnDate, 
                               Integer durationDays, BigDecimal basePrice, String status, 
                               Ship ship, Port departurePort, Port arrivalPort) {
        Cruise cruise = new Cruise();
        cruise.setCruiseName(cruiseName);
        cruise.setDepartureDate(departureDate);
        cruise.setReturnDate(returnDate);
        cruise.setDurationDays(durationDays);
        cruise.setBasePrice(basePrice);
        cruise.setStatus(status);
        cruise.setShip(ship);
        cruise.setDeparturePort(departurePort);
        cruise.setArrivalPort(arrivalPort);
        cruise.setCreatedAt(LocalDateTime.now());
        return cruise;
    }

    private void createAndSaveCruiseCabins(Cruise cruise, CabinType cabinType, 
                                          String cabinNumber, String deck, Integer availableQuantity, 
                                          BigDecimal priceMultiplier, String viewType) {
        CruiseCabin cruiseCabin = new CruiseCabin();
        cruiseCabin.setCruise(cruise);
        cruiseCabin.setCabinType(cabinType);
        cruiseCabin.setCabinNumber(cabinNumber);
        cruiseCabin.setDeck(deck);
        cruiseCabin.setAvailableQuantity(availableQuantity);
        cruiseCabin.setPriceMultiplier(priceMultiplier);
        cruiseCabin.setViewType(viewType);
        cruiseCabinRepository.save(cruiseCabin);
    }
}
