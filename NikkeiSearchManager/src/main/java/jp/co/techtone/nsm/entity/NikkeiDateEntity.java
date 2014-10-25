package jp.co.techtone.nsm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="nikkei_day_date")
@NamedQueries({
	@NamedQuery(name = "NikkeiDateEntity.getAll", query = "from NikkeiDateEntity"),
	@NamedQuery(name = "NikkeiDateEntity.findByDate", query = "from NikkeiDateEntity where day_date = :date"),
	@NamedQuery(name = "NikkeiDateEntity.findAverage", query="from NikkeiDateEntity nde where nde.day_date between :startDate and :endDate")
})
public class NikkeiDateEntity {

	@Id
	@Column
	private String day_date;

	@Column(nullable = false)
	private double open_rate;

	@Column(nullable = false)
	private double low_price;

	@Column(nullable = false)
	private double high_price;

	@Column(nullable = false)
	private double close_rate;

	public String getDay_date() {
		return day_date;
	}

	public void setDay_date(String day_date) {
		this.day_date = day_date;
	}

	public double getOpen_rate() {
		return open_rate;
	}

	public void setOpen_rate(double open_rate) {
		this.open_rate = open_rate;
	}

	public double getLow_price() {
		return low_price;
	}

	public void setLow_price(double low_price) {
		this.low_price = low_price;
	}

	public double getHigh_price() {
		return high_price;
	}

	public void setHigh_price(double high_price) {
		this.high_price = high_price;
	}

	public double getClose_rate() {
		return close_rate;
	}

	public void setClose_rate(double close_rate) {
		this.close_rate = close_rate;
	}


}
