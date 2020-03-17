package com.hedvig.productPricing.service;

import org.hibernate.dialect.PostgreSQL94Dialect;
import org.hibernate.type.descriptor.sql.BinaryTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

public class PostgresDialect extends PostgreSQL94Dialect {


    public PostgresDialect() {
        super();
        this.registerColumnType(Types.BLOB, "BYTEA");
    }

    @Override
    public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor descriptor) {
        if(descriptor.getSqlType() == Types.BLOB) {
            return BinaryTypeDescriptor.INSTANCE;
        }

        return super.remapSqlTypeDescriptor(descriptor);
    }
}
