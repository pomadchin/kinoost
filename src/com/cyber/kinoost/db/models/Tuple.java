package com.cyber.kinoost.db.models;

public class Tuple<F, S> {
	private F fst;
	private S snd;
	
	public Tuple(F fst, S snd) {
		this.fst = fst;
		this.snd = snd;
	}

	public F getFst() {
		return fst;
	}

	public void setFst(F fst) {
		this.fst = fst;
	}

	public S getSnd() {
		return snd;
	}

	public void setSnd(S snd) {
		this.snd = snd;
	}

	@Override
	public String toString() {
		return "Tuple [fst=" + fst + ", snd=" + snd + "]";
	}	
}
