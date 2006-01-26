package org.essentialplatform.runtime.shared.util;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.log4j.Logger;

public final class JmsUtil {

	private static Logger LOG = Logger.getLogger(JmsUtil.class);
	private static Logger getLogger() {
		return LOG;
	}
	
	private JmsUtil() {}
	
	public static void closeQuietly(final Session session) {
		if (session == null) return;
		try {
			session.close();
		} catch (JMSException ex) {
			// swallow
			getLogger().info("Exception closing session '" + session + "' - continuing.");
		}
	}

	public static void closeQuietly(MessageConsumer consumer) {
		if (consumer == null) return;
		try {
			consumer.close();
		} catch (JMSException ex) {
			// swallow
			getLogger().info("Exception closing consumer '" + consumer + "' - continuing.");
		}
	}

	public static void closeQuietly(MessageProducer producer) {
		if (producer == null) return;
		try {
			producer.close();
		} catch (JMSException ex) {
			// swallow
			getLogger().info("Exception closing producer '" + producer + "' - continuing.");
		}
	}

	public static void closeQuietly(Connection connection) {
		if (connection == null) return;
		try {
			connection.close();
		} catch (JMSException ex) {
			// swallow
			getLogger().info("Exception closing connection '" + connection + "' - continuing.");
		}
	}

}
