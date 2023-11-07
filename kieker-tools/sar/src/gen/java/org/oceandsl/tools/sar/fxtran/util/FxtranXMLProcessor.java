/**
 */
package org.oceandsl.tools.sar.fxtran.util;

import java.util.Map;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;

import org.oceandsl.tools.sar.fxtran.FxtranPackage;

/**
 * This class contains helper methods to serialize and deserialize XML documents <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 *
 * @generated
 */
public class FxtranXMLProcessor extends XMLProcessor {

    /**
     * Public constructor to instantiate the helper. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public FxtranXMLProcessor() {
        super(EPackage.Registry.INSTANCE);
        FxtranPackage.eINSTANCE.eClass();
    }

    /**
     * Register for "*" and "xml" file extensions the FxtranResourceFactoryImpl factory. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected Map<String, Resource.Factory> getRegistrations() {
        if (this.registrations == null) {
            super.getRegistrations();
            this.registrations.put(XML_EXTENSION, new FxtranResourceFactoryImpl());
            this.registrations.put(STAR_EXTENSION, new FxtranResourceFactoryImpl());
        }
        return this.registrations;
    }

} // FxtranXMLProcessor
