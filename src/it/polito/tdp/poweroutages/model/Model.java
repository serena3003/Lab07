package it.polito.tdp.poweroutages.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.poweroutages.db.PowerOutageDAO;

public class Model {

	PowerOutageDAO podao;
	List<Nerc> nerc;
	List<PowOutages> res;
	int maxPeopleAffected;

	public Model() {
		podao = new PowerOutageDAO();
		nerc = new ArrayList<Nerc>();
	}

	public List<Nerc> getNercList() {
		return podao.getNercList();
	}

	public List<PowOutages> faiAnalisi(Nerc nerc, int years, int hours) {
		List<PowOutages> poList = podao.getPowOutages(nerc.getId()); // prendo tutti i blackout avvenuti in quel nerc
		List<PowOutages> parziale = new ArrayList<>();
		this.res = new ArrayList<PowOutages>();
		maxPeopleAffected = 0;
		cerca(parziale, poList, years, hours);
		return res;
	}

	/*public void cerca(List<PowOutages> parziale, List<PowOutages> poList, int years, int hours, int L) {

		// casi terminali

		if (L == poList.size()) {
			return;
		}
		if(calcolaAnno(parziale) <= years && calcolaOre(parziale) <= hours) {
				System.out.println(parziale.toString() + "\n");
				this.res = new ArrayList<PowOutages>(parziale);
				return;
		}


		// genero sotto-problemi
		// blackout[L] è da aggiungere?
		// non lo aggiungo
		cerca(parziale, poList, years, hours, L + 1);

		// lo aggiungo
		parziale.add(poList.get(L));
		cerca(parziale, poList, years, hours, L + 1);
		parziale.remove(poList.get(L));
		
	}*/
	public void cerca(List<PowOutages> parziale, List<PowOutages> poList, int years, int hours) {

		// casi terminali
		if(sumMaxPeopleAffected(parziale) > this.maxPeopleAffected) {
			this.maxPeopleAffected = sumMaxPeopleAffected(parziale);
			this.res = new ArrayList<PowOutages>(parziale);
		}

		// lo aggiungo
		for(PowOutages po : poList) {
			if(!parziale.contains(po)) {
				parziale.add(po);
				if(calcolaAnno(parziale) <= years && calcolaOre(parziale) <= hours) {
					cerca(parziale, poList, years, hours);
				}
				parziale.remove(po);
			}
		}	
	}
	
	public int sumMaxPeopleAffected(List<PowOutages> poList) {
		int sum = 0;
		for (PowOutages po : poList) {
			sum += po.getCustomer();
		}
		return sum;
	}

	public int calcolaOre(List<PowOutages> poList) {
		int sum = 0;
		for (PowOutages po : poList) {
 
			sum += po.getDateStart().until(po.getDateEnd(), ChronoUnit.HOURS);
		}
		return sum;
	}
	

	public int calcolaAnno(List<PowOutages> poList) {

			LocalDate recentEnd = poList.get(0).getDateEnd().toLocalDate();
			LocalDate oldStart = poList.get(0).getDateStart().toLocalDate();

			for (PowOutages po : poList) {
				LocalDate dt = po.getDateEnd().toLocalDate();
				if (dt.isAfter(recentEnd)) {
					recentEnd = dt;
				}
				if (dt.isBefore(oldStart)) {
					oldStart = dt;
				}
			}
			return recentEnd.getYear() - oldStart.getYear();

	}
}
