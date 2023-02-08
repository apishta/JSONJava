CREATE TABLE source
(
    id bigserial NOT NULL,
    url character varying(4000),
    name character varying(256),
    CONSTRAINT source_pkey PRIMARY KEY (id)
);


CREATE TABLE plan
(
    id bigserial NOT NULL,
    idsource bigint,
    plantype character varying(256),
    planid character varying(256),
    markettype character varying(256),
    name character varying(256),
    CONSTRAINT plan_pkey PRIMARY KEY (id),
    CONSTRAINT fk_plan_idsource FOREIGN KEY (idsource)
        REFERENCES source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE sourcefile
(
    id bigserial NOT NULL,
    idsource bigint NOT NULL,
    idplan bigint,
    filedesc character varying(512),
    location character varying(512),
    srcfilename character varying(512),
    destfilename character varying(512),
    downloadedstatus integer,
    processedstatus integer,
    CONSTRAINT sourcefile_pkey PRIMARY KEY (idsource),
    CONSTRAINT fk_sourcefile_idplan FOREIGN KEY (idplan)
        REFERENCES plan (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID,
    CONSTRAINT fk_sourcefile_idsource FOREIGN KEY (idsource)
        REFERENCES source (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);
