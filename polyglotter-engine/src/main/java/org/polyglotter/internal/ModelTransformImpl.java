/*
 * Polyglotter (http://polyglotter.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.  Some portions may be licensed
 * to Red Hat, Inc. under one or more contributor license agreements.
 * See the AUTHORS.txt file in the distribution for a full listing of 
 * individual contributors.
 *
 * Polyglotter is free software. Unless otherwise indicated, all code in Polyglotter
 * is licensed to you under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * Polyglotter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.polyglotter.internal;

import java.util.LinkedList;

import org.polyglotter.ModelTransform;
import org.polyglotter.Transform;
import org.polyglotter.common.CheckArg;
import org.polyglotter.common.PolyglotterException;

/**
 * 
 */
public class ModelTransformImpl implements ModelTransform {

    private final LinkedList< Transform > transforms = new LinkedList<>();

    /**
     * {@inheritDoc}
     * 
     * @see ModelTransform#add(Transform)
     */
    @Override
    public void add( final Transform transform ) {
        CheckArg.notNull( transform, "transform" );
        transforms.add( transform );
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTransform#execute()
     */
    @Override
    public void execute() throws PolyglotterException {
        for ( final Transform xform : transforms )
            xform.execute();
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTransform#remove(Transform)
     */
    @Override
    public void remove( final Transform transform ) {
        CheckArg.notNull( transform, "transform" );
        transforms.remove( transform );
    }

    /**
     * {@inheritDoc}
     * 
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return transforms.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see ModelTransform#transforms()
     */
    @Override
    public Transform[] transforms() {
        return transforms.toArray( new Transform[ transforms.size() ] );
    }
}
