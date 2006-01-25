package org.essentialplatform.runtime.client.remoting;

import org.apache.log4j.Logger;
import org.essentialplatform.runtime.shared.remoting.AbstractRemoting;
import org.essentialplatform.runtime.shared.remoting.packaging.IPackager;
import org.essentialplatform.runtime.shared.remoting.packaging.ITransactionPackage;
import org.essentialplatform.runtime.shared.remoting.packaging.standard.StandardPackager;
import org.essentialplatform.runtime.shared.transaction.ITransaction;

public final class ClientRemoting extends AbstractRemoting {


	/*
	 * @see org.essentialplatform.runtime.shared.remoting.AbstractRemoting#getLogger()
	 */
	protected Logger getLogger() {
		return Logger.getLogger(ClientRemoting.class);
	}



	/*
	 * @see org.essentialplatform.runtime.shared.remoting.IRemoting#start()
	 */
	public void start() {
		getTransport().start();
	}

	/*
	 * @see org.essentialplatform.runtime.shared.remoting.IRemoting#stop()
	 */
	public void stop() {
		getTransport().shutdown();
	}


	public void send(ITransaction xactn) {
		getPackager().optimize(getMarshalling());
        ITransactionPackage packagedXactn = getPackager().pack(xactn);
		String marshalledXactn = getMarshalling().marshal(packagedXactn);
		getTransport().send(marshalledXactn);
	}

	
	private IPackager _packager = new StandardPackager();
	public IPackager getPackager() {
		return _packager;
	}
	public void setPackager(IPackager packager) {
		_packager = packager;
	}
	




}
