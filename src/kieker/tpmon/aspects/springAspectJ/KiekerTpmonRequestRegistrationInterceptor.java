package kieker.tpmon.aspects.springAspectJ;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * kieker.tpmon.aspects.springAspectJ.KiekerTpmonRequestRegistrationInterceptor
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 *
 * @author Andre van Hoorn
 */
public class KiekerTpmonRequestRegistrationInterceptor implements WebRequestInterceptor {

    private static final TpmonController ctrlInst = TpmonController.getInstance();

    @TpmonInternal()
    public void preHandle(WebRequest request) throws Exception {
        ctrlInst.getAndStoreUniqueThreadLocalTraceId();
        ctrlInst.storeThreadLocalSessionId(request.getSessionId());
        ctrlInst.storeThreadLocalEOI(0);
        ctrlInst.storeThreadLocalESS(1);
    }

    @TpmonInternal()
    public void postHandle(WebRequest request, ModelMap map) throws Exception {
        ctrlInst.unsetThreadLocalTraceId();
        ctrlInst.unsetThreadLocalSessionId();
        ctrlInst.unsetThreadLocalEOI();
        ctrlInst.unsetThreadLocalESS();
    }

    @TpmonInternal()
    public void afterCompletion(WebRequest request, Exception map) throws Exception {
    }
}
