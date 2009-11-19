/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ibatis.jpetstore.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kieker.tpmon.annotation.TpmonExecutionMonitoringProbe;
import org.apache.struts.action.ActionServlet;

/**
 *
 * @author Andre van Hoorn
 */
public class DispatcherServlet extends ActionServlet {

    @TpmonExecutionMonitoringProbe
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doGet(request, response);
        //System.out.println("doGet");
    }

    @TpmonExecutionMonitoringProbe
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.doPost(request, response);
        //System.out.println("doPost");
    }

    @TpmonExecutionMonitoringProbe
    protected void process(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        super.process(request, response);
        //System.out.println("process");
    }
}
