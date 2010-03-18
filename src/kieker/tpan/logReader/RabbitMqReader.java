/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpan.logReader;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConnectionParameters;
import com.rabbitmq.client.QueueingConsumer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import kieker.common.logReader.AbstractKiekerMonitoringLogReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import kieker.common.logReader.LogReaderExecutionException;

import kieker.common.record.AbstractMonitoringRecord;
import java.util.logging.Level;
import java.util.logging.Logger;
import kieker.common.record.OperationExecutionRecord;
/**
 *
 * This reader is an alternative to the JMS reader. RabbitMQ
 * showed RabbitMQ high performance in experiments.
 *
 * @author matthias
 */
public class RabbitMqReader extends AbstractKiekerMonitoringLogReader {

    private String rmqHostName = "localhost";
    private String rmqVirtualHost = "kiekertest";
    private String rmqExchangeName = "myExchange";
    private String rmqQueueName = "myQueue";
    private String rmqUserName = "matthias";
    private String rmqRoutingKey = "routingKey";
    private String rmqPassword = "testmatthias";
    private int rmqPortNumber = 5672 ; // default 5672

    private static final Log log = LogFactory.getLog(JMSReader.class);

    /**
     *
     * @param rmqHostName
     * @param rmqVirtualHost
     * @param rmqExchangeName
     * @param rmqQueueName
     * @param rmqUserName
     * @param rmqRoutingKey
     * @param rmqPassword
     * @param rmqPortNumber
     */
    public RabbitMqReader(String rmqHostName, String rmqVirtualHost, String rmqExchangeName, String rmqQueueName,
            String rmqUserName, String rmqRoutingKey, String rmqPassword, int rmqPortNumber ) {
            initInstanceFromArgs(rmqHostName, rmqVirtualHost, rmqExchangeName, rmqQueueName,
            rmqUserName, rmqRoutingKey, rmqPassword, rmqPortNumber);  // throws IllegalArgumentException
    }

    /** Constructor for RabbitMqReader. Requires a subsequent call to the init
     *  method in order to specify the input directory using the parameters
     *  @a jmsServerLocation and @a jmsDestination. */
    public RabbitMqReader(){ }

    /** Valid key/value pair: jmsProviderUrl=tcp://localhost:3035/ | jmsDestination=queue1 */
    
    public void init(String initString) throws IllegalArgumentException {
        super.initVarsFromInitString(initString); // throws IllegalArgumentException

        rmqHostName = this.getInitProperty("rmqHostName", null);
        rmqVirtualHost = this.getInitProperty("rmqVirtualHost", null);
        rmqExchangeName = this.getInitProperty("rmqExchangeName", null);
        rmqQueueName = this.getInitProperty("rmqQueueName", null);
        rmqUserName =this.getInitProperty("rmqUserName", null);
        rmqRoutingKey =this.getInitProperty("rmqRoutingKey", null);
        rmqPassword = this.getInitProperty("rmqPassword", null);
        rmqPortNumber = Integer.parseInt(this.getInitProperty("rmqPortNumber", null));
         initInstanceFromArgs(rmqHostName, rmqVirtualHost, rmqExchangeName, rmqQueueName,
            rmqUserName, rmqRoutingKey, rmqPassword, rmqPortNumber);
    }

   private void initInstanceFromArgs(String rmqHostName, String rmqVirtualHost, String rmqExchangeName,
           String rmqQueueName, String rmqUserName, String rmqRoutingKey, String rmqPassword, int rmqPortNumber) {
        initInstanceFromArgs(rmqHostName, rmqVirtualHost, rmqExchangeName, rmqQueueName,
            rmqUserName, rmqRoutingKey, rmqPassword, rmqPortNumber);  // throws IllegalArgumentException
    }


    @Override
    /**
     * A call to this method is a blocking call.
     */
    public boolean execute() throws LogReaderExecutionException {
        boolean retVal = false;

        System.out.printf("Starting connection to the Rabbit MQ message broker at %s : %d. (User %s, Pass %s)\n",rmqHostName,rmqPortNumber,rmqUserName,rmqPassword);

        Channel channel = null;
        try {
            ConnectionParameters params = new ConnectionParameters();
            params.setUsername(rmqUserName);
            params.setPassword(rmqPassword);
            params.setVirtualHost(rmqVirtualHost);
            params.setRequestedHeartbeat(0);
            ConnectionFactory factory = new ConnectionFactory(params);
            Connection conn = factory.newConnection(rmqHostName, rmqPortNumber);
            System.out.printf("Successfully connect to the rabbit MQ message broker at %s : %d. \n", rmqHostName, rmqPortNumber);
            channel = conn.createChannel();
        } catch (IOException ex) {
            Logger.getLogger( this.getClass().getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error: Failed to connect to the rabbit MQ message broker.");
            System.out.println(ex.getMessage());
        }

        int numberOfMessagesReceived = 0;
        // Retrieving messages by subscription
        boolean noAck = false;
        QueueingConsumer consumer = new QueueingConsumer(channel);
        try {
            channel.basicConsume(rmqQueueName, noAck, consumer);

        System.out.printf("Listening for messages at queue %s\n", rmqQueueName);
        while (true) {
            QueueingConsumer.Delivery delivery;
            numberOfMessagesReceived++;
            try {
                delivery = consumer.nextDelivery();
            } catch (InterruptedException ie) {
                continue;
            }
            // (process the message components ...)
            String execRecordString = new String(delivery.getBody());

        try {
            //AbstractMonitoringRecord abs = AbstractMonitoringRecord.getInstance();
            //abs.initFromStringArray(toStringArray(execRecordString));
            int indexOfFirstTokenEnd = execRecordString.indexOf(";");
            String execRecStringWithoutFirstToken = execRecordString.substring(indexOfFirstTokenEnd+1, execRecordString.length());

            OperationExecutionRecord ker = new OperationExecutionRecord();
            if (true){
            throw new RuntimeException("tokenize string");
            }
            // OLD: ker.initFromString(execRecStringWithoutFirstToken);
            // System.out.println(execRecStringWithoutFirstToken);
            //    System.out.printf("Recieved (%d) and decoded KiekerExecutionRecord: %s \n", numberOfMessagesReceived, ker);
            deliverRecordToConsumers(ker);
            } catch (Exception e) {
                System.out.printf("Recieved (%d) but could not Decode as KiekerExecutionRecord: %s \n %s", numberOfMessagesReceived, execRecordString,e.getMessage());
                //     System.out.printf("Skipping Message (%d) (Not proper KiekerExecutionRecord) \n");
            }
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        }

        } catch (IOException ex) {
            Logger.getLogger(RabbitMqReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retVal;
    }


    
    private final String[] toStringArray(String stringSerialization){
        StringTokenizer stk = new StringTokenizer(stringSerialization,";");
        ArrayList<String> als = new ArrayList<String>();
        while (stk.hasMoreElements()) {
            als.add(stk.nextToken());
        }
        return als.toArray(new String[als.size()]);

    }

}
