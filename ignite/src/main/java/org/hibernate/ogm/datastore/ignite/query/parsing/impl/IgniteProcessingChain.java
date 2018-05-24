/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.ogm.datastore.ignite.query.parsing.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.ast.spi.AstProcessingChain;
import org.hibernate.hql.ast.spi.AstProcessor;
import org.hibernate.hql.ast.spi.EntityNamesResolver;
import org.hibernate.hql.ast.spi.QueryResolverProcessor;
import org.hibernate.ogm.datastore.ignite.logging.impl.Log;
import org.hibernate.ogm.datastore.ignite.logging.impl.LoggerFactory;

/**
 * @author Victor Kadachigov
 */
public class IgniteProcessingChain implements AstProcessingChain<IgniteQueryParsingResult> {

	private static final Log log = LoggerFactory.getLogger();

	private final QueryResolverProcessor resolverProcessor;
	private final IgniteQueryRenderer queryRenderer;
	private final IgniteQueryTreeRenderer queryTreeRenderer;

	IgniteProcessingChain(SessionFactoryImplementor sessionFactory, EntityNamesResolver entityNamesResolver, Map<String, Object> namedParameters) {
		IgnitePropertyHelper propertyHelper = new IgnitePropertyHelper( sessionFactory, entityNamesResolver );
		resolverProcessor = new QueryResolverProcessor( new IgniteQueryResolverDelegate( sessionFactory, propertyHelper ) );
		queryRenderer = new IgniteQueryRenderer( propertyHelper, namedParameters );
		queryTreeRenderer = new IgniteQueryTreeRenderer( propertyHelper, queryRenderer );
	}

	@Override
	public Iterator<AstProcessor> iterator() {
		return Arrays.asList( resolverProcessor, queryTreeRenderer ).iterator();
	}

	@Override
	public IgniteQueryParsingResult getResult() {
		return queryRenderer.getResult();
	}

}
