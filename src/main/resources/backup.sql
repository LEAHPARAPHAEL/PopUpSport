CREATE SCHEMA AdminDatabase;

SET SCHEMA AdminDatabase;

CREATE TABLE Clients (
    lastname VARCHAR(50) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    userPassword CHAR(60) NOT NULL,
    credit DOUBLE,
    vip BOOLEAN,
    PRIMARY KEY (username)
);

CREATE TABLE Admins (
    id IDENTITY PRIMARY KEY, -- IDENTITY replaces AUTO_INCREMENT in H2
    adminUsername VARCHAR(50) NOT NULL UNIQUE,
    adminPassword CHAR(60)
);

CREATE TABLE Products (
    id IDENTITY PRIMARY KEY,
    productName VARCHAR(100) NOT NULL UNIQUE,
    productDescription VARCHAR(100) NOT NULL,
    productBrand VARCHAR(50) NOT NULL,
    price DOUBLE,
    inStock INT,
    image BLOB,
    available BOOLEAN DEFAULT TRUE,
    colors VARCHAR(100),
    discount DOUBLE DEFAULT 0
);

CREATE TABLE CustomerOrders (
    orderID IDENTITY PRIMARY KEY,
    clientUsername VARCHAR(50),
    address VARCHAR(100),
    price DOUBLE,
    orderTime TIMESTAMP NOT NULL,
    estimatedDeliveryTime TIMESTAMP NOT NULL,
    deliveryTime TIMESTAMP NOT NULL,
    orderStatus VARCHAR(50),
    CONSTRAINT UNIQUE_CLIENTS_TIME UNIQUE (clientUsername, orderTime),
    CONSTRAINT FK_CLIENTS FOREIGN KEY (clientUsername) REFERENCES Clients(username)
);

CREATE TABLE Reservation (
    productID INT,
    quantity INT,
    clientUsername VARCHAR(50),
    reservationTime TIMESTAMP,
    PRIMARY KEY (clientUsername, productID),
    CONSTRAINT FK_USERNAME1 FOREIGN KEY (clientUsername) REFERENCES Clients(username),
    CONSTRAINT FK_PRODUCT1 FOREIGN KEY (productID) REFERENCES Products(id)
);

CREATE TABLE Basket (
    productID INT,
    quantity INT,
    clientUsername VARCHAR(50),
    PRIMARY KEY (clientUsername, productID),
    CONSTRAINT FK_USERNAME FOREIGN KEY (clientUsername) REFERENCES Clients(username),
    CONSTRAINT FK_PRODUCT FOREIGN KEY (productID) REFERENCES Products(id)
);

CREATE TABLE OrderContent (
    orderID INT NOT NULL,
    productID INT NOT NULL,
    quantity INT NOT NULL,
    priceWhenOrdered DOUBLE NOT NULL,
    PRIMARY KEY (orderID, productID),
    CONSTRAINT FK_PRODUCTS FOREIGN KEY (productID) REFERENCES Products(id),
    CONSTRAINT FK_ORDERS FOREIGN KEY (orderID) REFERENCES CustomerOrders(orderID)
);

CREATE TABLE Achievements (
	achievementID IDENTITY PRIMARY KEY,
	clientUsername VARCHAR(50),
	achievement VARCHAR(500),
	reward DOUBLE,
	picture BLOB,
	achievementDate TIMESTAMP,
	CONSTRAINT FK_USERNAME_ACHIEVEMENTS FOREIGN KEY (clientUsername) REFERENCES Clients(username)
);



INSERT INTO Admins(adminUsername, adminPassword) VALUES('admin', '$2a$12$GlCGFvvCiFk.zVmmgMs.UeEHqTqGGLKVW/5RWqvz5ZxHS8V5D7r8C');