/*
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * This software is made available by Red Hat, Inc. under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.xform;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the {@link XFormEngine}.
 */
public final class XFormEngineTest {
    
    private static final String REPO_CONFIG_FILE = "src/test/resources/config/xformTestRepository.json";
    
    private XFormEngine engine = null;
    
    /**
     * 
     */
    @Before
    public void constructEngine() {
        this.engine = new XFormEngine( REPO_CONFIG_FILE );
    }
    
    /**
     * @throws Exception
     */
    @Test
    public void shouldObtainSession() throws Exception {
        assertThat( this.engine.session(), is( notNullValue() ) );
    }
    
}
