package eu.sqooss.test.service.abstractmetric;

import org.osgi.framework.BundleContext;

import eu.sqooss.service.abstractmetric.AbstractMetric;
import eu.sqooss.service.abstractmetric.MetricDecl;
import eu.sqooss.service.abstractmetric.MetricDeclarations;
import eu.sqooss.service.db.Bug;
import eu.sqooss.service.db.Developer;
import eu.sqooss.service.db.MailingListThread;
import eu.sqooss.service.db.ProjectVersion;
import eu.sqooss.service.pa.PluginInfo;

@MetricDeclarations(metrics={
	    @MetricDecl(mnemonic="CONTRIB", descr="Developer Contribution Metric",
	            dependencies={}, 
	            activators={Developer.class})
	})

public class TestMetric extends AbstractMetric {

	protected TestMetric(BundleContext bc) {
		super(bc);
	}
	
	public boolean install() {
		boolean result = super.install();
		if(result) {
			addConfigEntry("testconfiguration", 
	                "5" , 
	                "Number of committed files above which the developer is " +
	                "penalized", 
	                PluginInfo.ConfigurationType.INTEGER);
		}
		
		return result;
	}

}
