CREATE FOREIGN TABLE BookingCollection (
	carrid string(3) NOT NULL OPTIONS (ANNOTATION 'Airline'),
	connid string(4) NOT NULL OPTIONS (ANNOTATION 'Flight Number'),
	fldate timestamp NOT NULL OPTIONS (ANNOTATION 'Date'),
	bookid string(8) NOT NULL OPTIONS (ANNOTATION 'Booking number'),
	CUSTOMID string(8) NOT NULL OPTIONS (ANNOTATION 'Customer Number'),
	CUSTTYPE string(1) NOT NULL OPTIONS (ANNOTATION 'B/P customer'),
	SMOKER string(1) NOT NULL OPTIONS (ANNOTATION 'Smoker'),
	WUNIT string(3) NOT NULL OPTIONS (ANNOTATION 'Unit of measure'),
	LUGGWEIGHT bigdecimal NOT NULL OPTIONS (ANNOTATION 'Luggage Weight'),
	INVOICE string(1) NOT NULL OPTIONS (ANNOTATION 'Invoice pty.'),
	CLASS string(1) NOT NULL OPTIONS (ANNOTATION 'Class'),
	FORCURAM bigdecimal NOT NULL OPTIONS (ANNOTATION 'Amount'),
	FORCURKEY string(5) NOT NULL OPTIONS (ANNOTATION 'Paymnt currency'),
	LOCCURAM bigdecimal NOT NULL OPTIONS (ANNOTATION 'Amount'),
	LOCCURKEY string(5) NOT NULL OPTIONS (ANNOTATION 'Airline Currency'),
	ORDER_DATE timestamp NOT NULL OPTIONS (ANNOTATION 'Booking date', UPDATABLE FALSE),
	COUNTER string(8) NOT NULL OPTIONS (ANNOTATION 'Sales office', UPDATABLE FALSE),
	AGENCYNUM string(8) NOT NULL OPTIONS (ANNOTATION 'Agency No.', UPDATABLE FALSE),
	CANCELLED string(1) NOT NULL OPTIONS (ANNOTATION 'Cancelation flag', UPDATABLE FALSE),
	RESERVED string(1) NOT NULL OPTIONS (ANNOTATION 'Reserved', UPDATABLE FALSE),
	PASSNAME string(25) NOT NULL OPTIONS (ANNOTATION 'Passenger Name'),
	PASSFORM string(15) NOT NULL OPTIONS (ANNOTATION 'Title'),
	PASSBIRTH timestamp NOT NULL OPTIONS (ANNOTATION 'DOB of Passeng.'),
	PRIMARY KEY(carrid, connid, fldate, bookid)
) OPTIONS (UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Booking');

CREATE FOREIGN TABLE CarrierCollection (
	carrid string(3) NOT NULL OPTIONS (ANNOTATION 'Airline'),
	CARRNAME string(20) NOT NULL OPTIONS (ANNOTATION 'Airline'),
	CURRCODE string(5) NOT NULL OPTIONS (ANNOTATION 'Airline Currency'),
	URL string(255) NOT NULL OPTIONS (ANNOTATION 'URL'),
	mimeType string(128) NOT NULL OPTIONS (ANNOTATION 'MIME Type', SEARCHABLE 'Unsearchable'),
	PRIMARY KEY(carrid)
) OPTIONS ("teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Carrier');

CREATE FOREIGN TABLE FlightCollection (
	flightDetails_countryFrom string(3) NOT NULL OPTIONS (ANNOTATION 'Country', NAMEINSOURCE 'countryFrom', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_cityFrom string(20) NOT NULL OPTIONS (ANNOTATION 'Depart.city', NAMEINSOURCE 'cityFrom', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_airportFrom string(3) NOT NULL OPTIONS (ANNOTATION 'Dep. airport', NAMEINSOURCE 'airportFrom', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_countryTo string(3) NOT NULL OPTIONS (ANNOTATION 'Country', NAMEINSOURCE 'countryTo', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_cityTo string(20) NOT NULL OPTIONS (ANNOTATION 'Arrival city', NAMEINSOURCE 'cityTo', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_airportTo string(3) NOT NULL OPTIONS (ANNOTATION 'Dest. airport', NAMEINSOURCE 'airportTo', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_flightTime integer NOT NULL OPTIONS (ANNOTATION 'Flight time', NAMEINSOURCE 'flightTime', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_departureTime time NOT NULL OPTIONS (ANNOTATION 'Departure', NAMEINSOURCE 'departureTime', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_arrivalTime time NOT NULL OPTIONS (ANNOTATION 'Arrival Time', NAMEINSOURCE 'arrivalTime', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_distance bigdecimal NOT NULL OPTIONS (ANNOTATION 'Distance', NAMEINSOURCE 'distance', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_distanceUnit string(3) NOT NULL OPTIONS (ANNOTATION 'Distance in', NAMEINSOURCE 'distanceUnit', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_flightType string(1) NOT NULL OPTIONS (ANNOTATION 'Charter', NAMEINSOURCE 'flightType', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	flightDetails_period byte NOT NULL OPTIONS (ANNOTATION 'n day(s) later', NAMEINSOURCE 'period', "teiid_odata:ColumnGroup" 'flightDetails', "teiid_odata:ComplexType" 'FlightDetails'),
	carrid string(3) NOT NULL OPTIONS (ANNOTATION 'Airline'),
	connid string(4) NOT NULL OPTIONS (ANNOTATION 'Flight Number'),
	fldate timestamp NOT NULL OPTIONS (ANNOTATION 'Date'),
	PRICE bigdecimal NOT NULL OPTIONS (ANNOTATION 'Airfare'),
	CURRENCY string(5) NOT NULL OPTIONS (ANNOTATION 'Airline Currency', SEARCHABLE 'Unsearchable'),
	PLANETYPE string(10) NOT NULL OPTIONS (ANNOTATION 'Type of the plane'),
	SEATSMAX integer NOT NULL OPTIONS (ANNOTATION 'Max. capacity econ.'),
	SEATSOCC integer NOT NULL OPTIONS (ANNOTATION 'Occupied econ.'),
	PAYMENTSUM bigdecimal NOT NULL OPTIONS (ANNOTATION 'Total'),
	SEATSMAX_B integer NOT NULL OPTIONS (ANNOTATION 'Max. capacity bus.'),
	SEATSOCC_B integer NOT NULL OPTIONS (ANNOTATION 'Occupied bus.'),
	SEATSMAX_F integer NOT NULL OPTIONS (ANNOTATION 'Max. capacity 1st'),
	SEATSOCC_F integer NOT NULL OPTIONS (ANNOTATION 'Occupied 1st'),
	PRIMARY KEY(carrid, connid, fldate)
) OPTIONS (UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Flight');

CREATE FOREIGN TABLE NotificationCollection (
	ID string(32) NOT NULL,
	collection string(40) NOT NULL,
	title string NOT NULL,
	updated timestamp NOT NULL OPTIONS (ANNOTATION 'Time Stamp'),
	changeType string(30) NOT NULL OPTIONS (ANNOTATION 'Change Type'),
	entriesOfInterest integer NOT NULL OPTIONS (ANNOTATION 'No. of Entries'),
	recipient string(112) NOT NULL OPTIONS (ANNOTATION 'Recipient'),
	PRIMARY KEY(ID)
) OPTIONS ("teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Notification');

CREATE FOREIGN TABLE SubscriptionCollection (
	ID string(32) NOT NULL OPTIONS (UPDATABLE FALSE, SEARCHABLE 'Unsearchable'),
	"user" string(12) NOT NULL OPTIONS (ANNOTATION 'User Name', UPDATABLE FALSE, SEARCHABLE 'Unsearchable'),
	updated timestamp NOT NULL OPTIONS (ANNOTATION 'Time Stamp', UPDATABLE FALSE, SEARCHABLE 'Unsearchable'),
	title string(255) NOT NULL OPTIONS (SEARCHABLE 'Unsearchable'),
	deliveryAddress string NOT NULL OPTIONS (SEARCHABLE 'Unsearchable'),
	persistNotifications boolean NOT NULL OPTIONS (ANNOTATION 'Persist Notification', SEARCHABLE 'Unsearchable'),
	collection string(40) NOT NULL OPTIONS (SEARCHABLE 'Unsearchable'),
	"filter" string NOT NULL OPTIONS (SEARCHABLE 'Unsearchable'),
	"select" string(255) NOT NULL OPTIONS (SEARCHABLE 'Unsearchable'),
	changeType string(30) NOT NULL OPTIONS (ANNOTATION 'Change Type'),
	PRIMARY KEY(ID)
) OPTIONS (UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Subscription');

CREATE FOREIGN TABLE TravelAgencies (
	agencynum string(8) NOT NULL OPTIONS (ANNOTATION 'Agency No.'),
	NAME string(25) NOT NULL OPTIONS (ANNOTATION 'Travel agency name'),
	STREET string(30) NOT NULL OPTIONS (ANNOTATION 'Street'),
	POSTBOX string(10) NOT NULL OPTIONS (ANNOTATION 'PO Box'),
	POSTCODE string(10) NOT NULL OPTIONS (ANNOTATION 'Postal Code'),
	CITY string(25) NOT NULL OPTIONS (ANNOTATION 'City'),
	COUNTRY string(3) NOT NULL OPTIONS (ANNOTATION 'Country'),
	REGION string(3) NOT NULL OPTIONS (ANNOTATION 'Region'),
	TELEPHONE string(30) NOT NULL OPTIONS (ANNOTATION 'Tel.'),
	URL string(255) NOT NULL OPTIONS (ANNOTATION 'Travel agency URL'),
	LANGU string(2) NOT NULL OPTIONS (ANNOTATION 'Language'),
	CURRENCY string(5) NOT NULL OPTIONS (ANNOTATION 'Trav.Agency.Curr'),
	mimeType string(128) NOT NULL OPTIONS (ANNOTATION 'MIME Type'),
	PRIMARY KEY(agencynum)
) OPTIONS (UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Travelagency');

CREATE FOREIGN TABLE TravelagencyCollection (
	agencynum string(8) NOT NULL OPTIONS (ANNOTATION 'Agency No.'),
	NAME string(25) NOT NULL OPTIONS (ANNOTATION 'Travel agency name'),
	STREET string(30) NOT NULL OPTIONS (ANNOTATION 'Street'),
	POSTBOX string(10) NOT NULL OPTIONS (ANNOTATION 'PO Box'),
	POSTCODE string(10) NOT NULL OPTIONS (ANNOTATION 'Postal Code'),
	CITY string(25) NOT NULL OPTIONS (ANNOTATION 'City'),
	COUNTRY string(3) NOT NULL OPTIONS (ANNOTATION 'Country'),
	REGION string(3) NOT NULL OPTIONS (ANNOTATION 'Region'),
	TELEPHONE string(30) NOT NULL OPTIONS (ANNOTATION 'Tel.'),
	URL string(255) NOT NULL OPTIONS (ANNOTATION 'Travel agency URL'),
	LANGU string(2) NOT NULL OPTIONS (ANNOTATION 'Language'),
	CURRENCY string(5) NOT NULL OPTIONS (ANNOTATION 'Trav.Agency.Curr'),
	mimeType string(128) NOT NULL OPTIONS (ANNOTATION 'MIME Type'),
	PRIMARY KEY(agencynum)
) OPTIONS (ANNOTATION 'Travel Agencies', UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Travelagency');