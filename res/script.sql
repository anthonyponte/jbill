CREATE TABLE SUMMARY (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0, INCREMENT BY 1) PRIMARY KEY,
    UBL VARCHAR(10) NOT NULL,
    VERSION VARCHAR(10) NOT NULL,
    TIPO_CODIGO VARCHAR(2) NOT NULL,
    TIPO_DESCRIPCION VARCHAR(100) NOT NULL,
    SERIE VARCHAR(8) NOT NULL,
    CORRELATIVO INTEGER NOT NULL,
    FECHA_EMISION DATE NOT NULL,
    FECHA_REFERENCIA DATE NOT NULL,
    RUC VARCHAR(11) NOT NULL,
    RUC_TIPO INTEGER NOT NULL,
    RAZON_SOCIAL VARCHAR(100) NOT NULL,
    ZIP_NOMBRE VARCHAR(33) NOT NULL,
    ZIP BLOB(1024) NOT NULL,
    TICKET VARCHAR(100),
    STATUS_CODE VARCHAR(2),
    CONTENT_NOMBRE VARCHAR(35),
    CONTENT BLOB(2048),
    FECHA_INGRESO TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE COMUNICACION_BAJA_DETALLE (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0, INCREMENT BY 1) PRIMARY KEY,
    SUMMARY_ID INTEGER NOT NULL,
    NUMERO INTEGER NOT NULL,
    TIPO_CODIGO VARCHAR(2) NOT NULL,
    TIPO_DESCRIPCION VARCHAR(100) NOT NULL,
    SERIE VARCHAR(4) NOT NULL,
    CORRELATIVO INTEGER NOT NULL,
    MOTIVO VARCHAR(100) NOT NULL,
    FECHA_INGRESO TIMESTAMP DEFAULT NOW() NOT NULL
);

ALTER TABLE
    COMUNICACION_BAJA_DETALLE
ADD
    FOREIGN KEY (SUMMARY_ID) REFERENCES SUMMARY (ID);

CREATE TABLE RESUMEN_DIARIO_DETALLE (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY(START WITH 0, INCREMENT BY 1) PRIMARY KEY,
    SUMMARY_ID INTEGER NOT NULL,
    NUMERO INTEGER NOT NULL,
    SERIE VARCHAR(4) NOT NULL,
    CORRELATIVO INTEGER NOT NULL,
    TIPO_CODIGO VARCHAR(2) NOT NULL,
    TIPO_DESCRIPCION VARCHAR(100) NOT NULL,
    DOCUMENTO_IDENTIDAD VARCHAR(11),
    DOCUMENTO_IDENTIDAD_TIPO VARCHAR(1),
    DOCUMENTO_IDENTIDAD_DESCRIPCION VARCHAR(100),
    REFERENCIA_SERIE VARCHAR(4),
    REFERENCIA_CORRELATIVO INTEGER,
    REFERENCIA_TIPO_CODIGO VARCHAR(2),
    REFERENCIA_TIPO_DESCRIPCION VARCHAR(100),
    PERCEPCION_REGIMEN_CODIGO VARCHAR(2),
    PERCEPCION_REGIMEN_DESCRIPCION VARCHAR(100),
    PERCEPCION_REGIMEN_PORCENTAJE FLOAT,
    PERCEPCION_MONTO FLOAT,
    PERCEPCION_MONTO_TOTAL FLOAT,
    PERCEPCION_BASE FLOAT,
    ESTADO_CODIGO INTEGER NOT NULL,
    ESTADO_DESCRIPCION VARCHAR(100) NOT NULL,
    IMPORTE_TOTAL FLOAT NOT NULL,
    MONEDA_CODIGO VARCHAR(3) NOT NULL,
    MONEDA_DESCRIPCION VARCHAR(100) NOT NULL,
    GRAVADAS_TOTAL FLOAT,
    GRAVADAS_CODIGO VARCHAR(2),
    GRAVADAS_DESCRIPCION VARCHAR(100),
    EXONERADAS_TOTAL FLOAT,
    EXONERADAS_CODIGO VARCHAR(2),
    EXONERADAS_DESCRIPCION VARCHAR(100),
    INAFECTAS_TOTAL FLOAT,
    INAFECTAS_CODIGO VARCHAR(2),
    INAFECTAS_DESCRIPCION VARCHAR(100),
    GRATUITAS_TOTAL FLOAT,
    GRATUITAS_CODIGO VARCHAR(2),
    GRATUITAS_DESCRIPCION VARCHAR(100),
    EXPORTACION_TOTAL FLOAT,
    EXPORTACION_CODIGO VARCHAR(2),
    EXPORTACION_DESCRIPCION VARCHAR(100),
    OTROS_CARGOS_INDICADOR BOOLEAN,
    OTROS_CARGOS_TOTAL FLOAT,
    IGV_TOTAL FLOAT NOT NULL,
    IGV_CODIGO VARCHAR(4) NOT NULL,
    IGV_DESCRIPCION VARCHAR(10) NOT NULL,
    IGV_CODIGO_INTERNACIONAL VARCHAR(3) NOT NULL,
    ISC_TOTAL FLOAT,
    ISC_CODIGO VARCHAR(4),
    ISC_DESCRIPCION VARCHAR(10),
    ISC_CODIGO_INTERNACIONAL VARCHAR(3),
    OTRO_TRIBUTOS_TOTAL FLOAT,
    OTRO_TRIBUTOS_CODIGO VARCHAR(4),
    OTRO_TRIBUTOS_DESCRIPCION VARCHAR(10),
    OTRO_TRIBUTOS_CODIGO_INTERNACIONAL VARCHAR(3),
    IMPUESTO_BOLSA_TOTAL FLOAT,
    IMPUESTO_BOLSA_CODIGO VARCHAR(4),
    IMPUESTO_BOLSA_DESCRIPCION VARCHAR(10),
    IMPUESTO_BOLSA_CODIGO_INTERNACIONAL VARCHAR(3),
    FECHA_INGRESO TIMESTAMP DEFAULT NOW() NOT NULL
);

ALTER TABLE
    RESUMEN_DIARIO_DETALLE
ADD
    FOREIGN KEY (SUMMARY_ID) REFERENCES SUMMARY (ID);
