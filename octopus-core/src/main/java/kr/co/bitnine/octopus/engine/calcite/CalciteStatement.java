/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kr.co.bitnine.octopus.engine.calcite;

import java.sql.SQLException;
import org.apache.calcite.avatica.AvaticaResultSet;
import org.apache.calcite.avatica.AvaticaStatement;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.jdbc.CalcitePrepare;
import org.apache.calcite.linq4j.Queryable;
import org.apache.calcite.server.CalciteServerStatement;

/**
 * Implementation of {@link java.sql.Statement}
 * for the Calcite engine.
 */
public abstract class CalciteStatement extends AvaticaStatement {
    /**
     * Creates a CalciteStatement.
     *
     * @param connection           Connection
     * @param h                    Statement handle
     * @param resultSetType        Result set type
     * @param resultSetConcurrency Result set concurrency
     * @param resultSetHoldability Result set holdability
     */
    CalciteStatement(CalciteConnectionImpl connection, Meta.StatementHandle h,
                     int resultSetType, int resultSetConcurrency, int resultSetHoldability) {
        super(connection, h, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    // implement Statement

    @Override
    public final <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface == CalciteServerStatement.class) {
            return iface.cast(getConnection().getServer().getStatement(handle));
        }
        return super.unwrap(iface);
    }

    @Override
    public final CalciteConnectionImpl getConnection() {
        return (CalciteConnectionImpl) connection;
    }

    public final CalciteConnectionImpl.ContextImpl createPrepareContext() {
        return new CalciteConnectionImpl.ContextImpl(getConnection());
    }

    protected final <T> CalcitePrepare.CalciteSignature<T> prepare(
            Queryable<T> queryable) {
        final CalciteConnectionImpl calciteConnection = getConnection();
        final CalcitePrepare prepare = calciteConnection.getPrepareFactory().apply();
        final CalciteServerStatement serverStatement =
                calciteConnection.getServer().getStatement(handle);
        final CalcitePrepare.Context prepareContext =
                serverStatement.createPrepareContext();
        return prepare.prepareQueryable(prepareContext, queryable);
    }

    @Override
    protected final void close_() {
        if (!closed) {
            closed = true;
            final CalciteConnectionImpl connection1 =
                    (CalciteConnectionImpl) connection;
            connection1.getServer().removeStatement(handle);
            if (openResultSet != null) {
                AvaticaResultSet c = openResultSet;
                openResultSet = null;
                c.close();
            }
            // If onStatementClose throws, this method will throw an exception (later
            // converted to SQLException), but this statement still gets closed.
            connection1.getDriver().handler.onStatementClose(this);
        }
    }
}
