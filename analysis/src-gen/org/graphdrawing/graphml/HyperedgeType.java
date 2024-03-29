//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.02.25 at 12:10:48 PM CET
//

package org.graphdrawing.graphml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * Complex type for the <hyperedge> element.
 *
 *
 * <p>
 * Java class for hyperedge.type complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="hyperedge.type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://graphml.graphdrawing.org/xmlns}desc" minOccurs="0"/>
 *         &lt;choice maxOccurs="unbounded" minOccurs="0">
 *           &lt;element ref="{http://graphml.graphdrawing.org/xmlns}data"/>
 *           &lt;element ref="{http://graphml.graphdrawing.org/xmlns}endpoint"/>
 *         &lt;/choice>
 *         &lt;element ref="{http://graphml.graphdrawing.org/xmlns}graph" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{http://graphml.graphdrawing.org/xmlns}hyperedge.extra.attrib"/>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}NMTOKEN" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "hyperedge.type", propOrder = {
	"desc",
	"dataOrEndpoint",
	"graph"
})
public class HyperedgeType {

	protected String desc;
	@XmlElements({
		@XmlElement(name = "data", type = DataType.class),
		@XmlElement(name = "endpoint", type = EndpointType.class)
	})
	protected List<Object> dataOrEndpoint;
	protected GraphType graph;
	@XmlAttribute(name = "id")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlSchemaType(name = "NMTOKEN")
	protected String id;

	/**
	 * Gets the value of the desc property.
	 *
	 * @return
	 *         possible object is
	 *         {@link String }
	 *
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * Sets the value of the desc property.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 *
	 */
	public void setDesc(final String value) {
		this.desc = value;
	}

	/**
	 * Gets the value of the dataOrEndpoint property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list,
	 * not a snapshot. Therefore any modification you make to the
	 * returned list will be present inside the JAXB object.
	 * This is why there is not a <CODE>set</CODE> method for the dataOrEndpoint property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getDataOrEndpoint().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link DataType }
	 * {@link EndpointType }
	 *
	 *
	 */
	public List<Object> getDataOrEndpoint() {
		if (this.dataOrEndpoint == null) {
			this.dataOrEndpoint = new ArrayList<>();
		}
		return this.dataOrEndpoint;
	}

	/**
	 * Gets the value of the graph property.
	 *
	 * @return
	 *         possible object is
	 *         {@link GraphType }
	 *
	 */
	public GraphType getGraph() {
		return this.graph;
	}

	/**
	 * Sets the value of the graph property.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link GraphType }
	 *
	 */
	public void setGraph(final GraphType value) {
		this.graph = value;
	}

	/**
	 * Gets the value of the id property.
	 *
	 * @return
	 *         possible object is
	 *         {@link String }
	 *
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 * @param value
	 *            allowed object is
	 *            {@link String }
	 *
	 */
	public void setId(final String value) {
		this.id = value;
	}

}
