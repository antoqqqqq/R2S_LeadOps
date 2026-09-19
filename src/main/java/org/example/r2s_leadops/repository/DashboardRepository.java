package org.example.r2s_leadops.repository;

import org.example.r2s_leadops.DTO.response.LeadCounterResponse;
import org.example.r2s_leadops.DTO.response.LeadResourceResponse;
import org.example.r2s_leadops.DTO.response.TopLeadsResponse;
import org.example.r2s_leadops.entity.LeadOpportunity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DashboardRepository extends JpaRepository<LeadOpportunity, Integer> {

    /**
     * Thống kê số lượng Lead theo từng status
     */
    @Query("""
        SELECT new org.example.r2s_leadops.DTO.response.LeadCounterResponse(
            COUNT(lo.id),
            SUM(CASE WHEN ls.code = 'NEW' THEN 1 ELSE 0 END),
            SUM(CASE WHEN ls.code = 'NURTURE' THEN 1 ELSE 0 END),
            SUM(CASE WHEN ls.code = 'WARM' THEN 1 ELSE 0 END),
            SUM(CASE WHEN ls.code = 'HOT' THEN 1 ELSE 0 END),
            SUM(CASE WHEN ls.code = 'SALE' THEN 1 ELSE 0 END),
            SUM(CASE WHEN ls.code = 'WON' THEN 1 ELSE 0 END),
            SUM(CASE WHEN ls.code = 'LOST' THEN 1 ELSE 0 END)
        )
        FROM LeadOpportunity lo
        LEFT JOIN lo.status ls
    """)
    LeadCounterResponse getLeadCounter();


    /**
     * Thống kê số lượng Lead theo source
     */
    @Query("""
        SELECT new org.example.r2s_leadops.DTO.response.LeadResourceResponse(
            CAST(ls.code AS string),
            COUNT(lo.id)
        )
        FROM LeadOpportunity lo
        JOIN lo.source ls
        GROUP BY ls.code
        ORDER BY COUNT(lo.id) DESC
    """)
    List<LeadResourceResponse> getLeadResource();


    /**
     * Top Lead có totalScore cao nhất
     */
    @Query("""
    SELECT new org.example.r2s_leadops.DTO.response.TopLeadsResponse(
        lo.id,
        l.fullName,
        lo.totalScore,
        CAST(ls.code AS string),
        u.id,
        u.fullName,
        c.name
    )
    FROM LeadOpportunity lo
    JOIN lo.lead l
    LEFT JOIN lo.status ls
    LEFT JOIN lo.assignedUser u
    JOIN lo.course c
    ORDER BY lo.totalScore DESC
""")
    List<TopLeadsResponse> getTopLeads(Pageable pageable);
}