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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ibatis.jpetstore.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.monitoring.annotation.OperationExecutionMonitoringProbe;
import org.apache.struts.action.ActionServlet;

/**
 *
 * @author Andre van Hoorn
 */
public class DispatcherServlet extends ActionServlet {

    @OperationExecutionMonitoringProbe
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doGet(request, response);
        //System.out.println("doGet");
    }

    @OperationExecutionMonitoringProbe
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doPost(request, response);
        //System.out.println("doPost");
    }

    @OperationExecutionMonitoringProbe
    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.process(request, response);
        //System.out.println("process");
    }
}
