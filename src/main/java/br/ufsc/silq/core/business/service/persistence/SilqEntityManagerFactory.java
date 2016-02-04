package br.ufsc.silq.core.business.service.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class SilqEntityManagerFactory {

	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("silqcore");

	public static EntityManager createEntityManager() {
		return emf.createEntityManager();
	}
}
