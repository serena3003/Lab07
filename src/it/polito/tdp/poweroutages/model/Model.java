package it.polito.tdp.poweroutages.model;

import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	
	public Model() {
		podao = new PowerOutageDAO();
	}
	
	public List<Nerc> getNercList() {
		return podao.getNercList();
	}
	
	public void cerca(Set<Nerc> parziale, Nerc nerc, int years, int hours, int L) {
		
		//casi terminali
		
		//genero sotto-problemi
		//blackout[L] è da aggiungere?
		//non lo aggiungo
		cerca(parziale, nerc, years, hours, L+1);
		
		//lo aggiungo
		parziale.add(nerc);
	}

}
