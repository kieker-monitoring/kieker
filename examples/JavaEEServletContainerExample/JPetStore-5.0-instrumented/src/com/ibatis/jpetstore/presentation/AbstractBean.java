/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package com.ibatis.jpetstore.presentation;

import org.apache.struts.beanaction.ActionContext;
import org.apache.struts.beanaction.BaseBean;

public abstract class AbstractBean extends BaseBean {

  public static final String SUCCESS = "success";
  public static final String FAILURE = "failure";
  public static final String SIGNON = "signon";
  public static final String SHIPPING = "shipping";
  public static final String CONFIRM = "confirm";

  protected void setMessage(String value) {
    ActionContext.getActionContext().getRequestMap().put("message", value);
  }
  
}
