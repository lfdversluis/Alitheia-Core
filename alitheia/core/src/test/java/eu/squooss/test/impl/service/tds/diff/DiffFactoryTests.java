package eu.squooss.test.impl.service.tds.diff;

import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import eu.sqooss.impl.service.fds.tests.CoreActivator;
import eu.sqooss.impl.service.tds.diff.DiffFactory;
import eu.sqooss.service.tds.Revision;

public class DiffFactoryTests {
	
	@BeforeClass
    public static void setUp() throws MalformedURLException {
    	new CoreActivator();
    }

	@Test
    public void testDoUnifiedDiff() {
		
		Revision start = Mockito.mock(Revision.class);
		Revision end = Mockito.mock(Revision.class);
		
		DiffFactory inst = DiffFactory.getInstance();
		assertNotNull(inst);
		DiffFactory.getInstance().doUnifiedDiff(start, end, "test", "Index: COMMITTERS\n===================================================================\n--- b.c (revision 1)\n+++ b.c (working copy)\n@@ -0,0 +1,6 @@\n+#include <stdio.h>\n+\n+int main()\n+{\n+    printf(\"Hello, world\");\n+}");
		DiffFactory.getInstance().doUnifiedDiff(null, null, "test", "test");
		assertNotNull(DiffFactory.getInstance());
    }
}
