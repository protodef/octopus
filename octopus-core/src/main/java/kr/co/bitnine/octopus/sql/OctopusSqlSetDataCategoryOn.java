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

package kr.co.bitnine.octopus.sql;

public class OctopusSqlSetDataCategoryOn extends OctopusSqlCommand {
    String dataSource;
    String schema;
    String table;
    String column;
    String category;

    public OctopusSqlSetDataCategoryOn(String dataSource, String schema, String table, String column, String category) {
        this.dataSource = dataSource;
        this.schema = schema;
        this.table = table;
        this.column = column;
        this.category = category;
    }

    @Override
    public OctopusSqlCommand.Type getType()
    {
        return Type.SET_DATACATEGORY_ON;
    }

    public String getDataSource() {
        return dataSource;
    }

    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public String getColumn() {
        return column;
    }

    public String getCategory() {
        return category;
    }
}