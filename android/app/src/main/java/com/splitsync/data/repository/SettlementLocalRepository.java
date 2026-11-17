package com.splitsync.data.repository;

import com.splitsync.data.local.dao.SettlementDao;
import com.splitsync.data.local.entity.SettlementEntity;
import com.splitsync.data.local.mapper.SettlementMapper;
import com.splitsync.domain.model.Settlement;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.SettlementId;

import java.util.ArrayList;
import java.util.List;

/**
 * Room-backed settlement persistence.
 */
public class SettlementLocalRepository {

    private final SettlementDao settlementDao;

    public SettlementLocalRepository(SettlementDao settlementDao) {
        this.settlementDao = settlementDao;
    }

    public void save(Settlement settlement) {
        settlementDao.insert(SettlementMapper.toEntity(settlement));
    }

    public void update(Settlement settlement) {
        settlementDao.update(SettlementMapper.toEntity(settlement));
    }

    public void delete(SettlementId settlementId) {
        settlementDao.deleteById(settlementId.value());
    }

    public Settlement findById(SettlementId settlementId) {
        SettlementEntity entity = settlementDao.findById(settlementId.value());
        return entity == null ? null : SettlementMapper.toDomain(entity);
    }

    public List<Settlement> findByGroupId(GroupId groupId) {
        List<Settlement> settlements = new ArrayList<>();
        for (SettlementEntity entity : settlementDao.findByGroupId(groupId.value())) {
            settlements.add(SettlementMapper.toDomain(entity));
        }
        return settlements;
    }
}
