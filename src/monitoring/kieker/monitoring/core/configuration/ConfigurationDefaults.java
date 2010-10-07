package kieker.monitoring.core.configuration;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
 *
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
 * ==================================================
 */

public class ConfigurationDefaults {
	public static final boolean DEBUG_ENABLED = true;
	public static final boolean MONITORING_ENABLED = true;

	public static final int ASYNC__RECORD_QUEUE_SIZE = 8000;

	public static final boolean ASYNC__BLOCK_ON_FULL_QUEUEU = false;

	public static final boolean FS_WRITER__STORE_IN_JAVAIOTMPDIR = true;
	public static final String FS_WRITER__CUSTOM_STORAGE_PATH = "/tmp";
	public static final String FS_FN_PREFIX = "";
	
	public static final int EXPERIMENT_ID = 0;
	
	public static final String DB__DRIVER_CLASSNAME = "com.mysql.jdbc.Driver";
	public static final String DB__CONNECTION_ADDRESS = "jdbc:mysql://HOSTNAME/DATABASENAME?user=DBUSER&password=DBPASS";
	public static final String DB__TABLE_NAME = "kieker";
	public static final boolean DB__SET_INITIAL_EXP_ID_BASED_ON_LAST = true;
}
