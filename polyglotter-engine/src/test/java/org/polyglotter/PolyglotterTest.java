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
package org.polyglotter;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link Polyglotter}.
 */
public final class PolyglotterTest {
    
    private Polyglotter engine;
    
    /**
     * 
     */
    @Before
    public void constructEngine() {
        this.engine = new Polyglotter();
        engine.setModeShapeConfigurationPath( "jcr/testModeShapeConfig.json" );
    }
    
    /**
     * @throws Throwable
     */
    @Test
    public void shouldObtainSession() throws Throwable {
        assertThat( this.engine.session(), is( notNullValue() ) );
        assertThat( TestLogger.warningMessages().isEmpty(), is( true ) );
        assertThat( TestLogger.errorMessages().isEmpty(), is( true ) );
    }
}
