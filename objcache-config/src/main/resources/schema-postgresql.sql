--#-------------------------------------------------------------------------------
--# Copyright 2018 Codeveo Ltd.
--# 
--# Written by Ladislav Klenovic <lklenovic@codeveo.com>
--# 
--# Licensed under the Apache License, Version 2.0 (the "License"); you may not
--# use this file except in compliance with the License.  You may obtain a copy
--# of the License at
--# 
--#   http://www.apache.org/licenses/LICENSE-2.0
--# 
--# Unless required by applicable law or agreed to in writing, software
--# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
--# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
--# License for the specific language governing permissions and limitations under
--# the License.
--#-------------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS public.t_objcache(
      id SERIAL PRIMARY KEY NOT NULL,
      collection_id VARCHAR(64) NOT NULL,
      object_key VARCHAR(64) NOT NULL,
      serializer_type VARCHAR(16) NOT NULL,
      version INTEGER NOT NULL,
      object_data TEXT,
      properties jsonb,
      expiration_time TIMESTAMP WITH TIME ZONE,
      
      UNIQUE(collection_id, object_key)
);

CREATE INDEX IF NOT EXISTS idx_collection_id ON public.t_objcache (collection_id);
CREATE INDEX IF NOT EXISTS idx_object_key ON public.t_objcache (object_key);
CREATE INDEX IF NOT EXISTS idx_collection_key ON public.t_objcache (collection_id, object_key);
CREATE INDEX IF NOT EXISTS idx_properties ON public.t_objcache USING gin (properties);

CREATE SEQUENCE IF NOT EXISTS public.objcache_sequence;
