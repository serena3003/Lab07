package it.polito.tdp.poweroutages.model;

import java.time.LocalDateTime;
import java.util.Date;

public class PowOutages {
	
	int id;
	int nercId;
	int customer;
	//Date dateStart;
	//Date dateEnd;
	LocalDateTime dateStart;
	LocalDateTime dateEnd;
	
	public PowOutages(int id, int nercId, int cust, LocalDateTime ds, LocalDateTime de) {
		this.id = id;
		this.nercId = nercId;
		this.customer = cust;
		this.dateStart = ds;
		this.dateEnd = de;
	}
	
	public int getCustomer() {
		return customer;
	}

	public LocalDateTime getDateStart() {
		return this.dateStart;
	}

	public LocalDateTime getDateEnd() {
		return this.dateEnd;
	}

	@Override
	public String toString() {
		return "dateStart=" + dateStart.toLocalDate() + ", dateEnd=" + dateEnd.toLocalDate() + ", customer=" + customer ;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PowOutages other = (PowOutages) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
