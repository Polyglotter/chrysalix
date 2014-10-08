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
	PRIMARY KEY(carrid, connid, fldate),
	CONSTRAINT CarrierToFlight FOREIGN KEY(carrid) REFERENCES CarrierCollection 
) OPTIONS (UPDATABLE TRUE, "teiid_odata:EntityType" 'RMTSAMPLEFLIGHT.Flight');