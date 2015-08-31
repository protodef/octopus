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

package kr.co.bitnine.octopus.engine;

import kr.co.bitnine.octopus.postgres.utils.adt.FormatCode;
import kr.co.bitnine.octopus.postgres.utils.cache.Portal;

public class Cursor extends Portal
{
    private final FormatCode[] paramFormats;
    private final byte[][] paramValues;
    private final FormatCode[] resultFormats;

    public Cursor(CachedStatement cachedStatement, FormatCode[] paramFormats, byte[][] paramValues, FormatCode[] resultFormats)
    {
        super(cachedStatement);

        this.paramFormats = paramFormats;
        this.paramValues = paramValues;
        this.resultFormats = resultFormats;
    }

    public CachedStatement getCachedStatement()
    {
        return (CachedStatement) getCachedQuery();
    }
}
