package com.medHead.poc.services;

import com.medHead.poc.entity.Hopital;

public interface ReserveServiceInterface {
    boolean reserveBed(Hopital hopital, boolean strictMode);
}
