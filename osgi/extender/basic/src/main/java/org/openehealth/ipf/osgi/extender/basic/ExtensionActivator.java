/*
 * Copyright 2009 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openehealth.ipf.osgi.extender.basic;

import static org.osgi.framework.BundleEvent.STARTED;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.SynchronousBundleListener;

/**
 * @author Martin Krasser
 */
public class ExtensionActivator implements BundleActivator, SynchronousBundleListener {

    private static final Log LOG = LogFactory.getLog(ExtensionActivator.class);
    
    @Override
    public void bundleChanged(BundleEvent event) {
        if (event.getType() == STARTED) {
            ExtensionClass.activateAll(event.getBundle());
        }
    }
    
    @Override
    public void start(BundleContext context) throws Exception {
        context.addBundleListener(this);
        LOG.debug("initialized extension activator");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        context.removeBundleListener(this);
        LOG.debug("destroyed extension activator");
    }
    
}
