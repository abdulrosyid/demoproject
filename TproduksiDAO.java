package com.sdd.demoproject.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.sdd.demoproject.domain.Tproduksi;
import com.sdd.demoproject.util.StoreHibernate;

public class TproduksiDAO {
	Session session;
	Transaction transaction;

	@SuppressWarnings("unchecked")
	public List<Tproduksi> list(String stFilter) throws Exception {
		session = StoreHibernate.openSession();
		
		List<Tproduksi> listProduksi = new ArrayList<>();
		
//		session.evict(listProduksi);
//		session.evict(Tproduksi.class);
//		session.clear();	
		
		listProduksi = session
				.createSQLQuery(
						"select * from Tproduksi order by date_produksi desc, id_m_ipp ")
				.addEntity(Tproduksi.class).list();
		
		session.close();
		return listProduksi;
	}
}
