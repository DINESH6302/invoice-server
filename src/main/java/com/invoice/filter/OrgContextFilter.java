package com.invoice.filter;

import com.invoice.context.OrgContext;
import com.invoice.exception.AccessDeniedException;
import com.invoice.exception.NotFountException;
import com.invoice.repositorie.OrgRepository;
import com.invoice.utils.UserUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class OrgContextFilter extends OncePerRequestFilter {

    private final OrgRepository orgRepository;

    @Autowired
    public OrgContextFilter(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/v1/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = req.getHeader("X-Org-Id");
            Long userId = UserUtil.getUserId();

            if (header == null) {
                throw new AccessDeniedException("X-Org-Id header is missing");
            }

            Long orgId = Long.parseLong(header);
            boolean hasAccess = orgRepository.existsByUser_UserIdAndOrgId(userId, orgId);

            if (!orgRepository.existsById(orgId)) {
                throw new NotFountException("Organisation not found.");
            } else if (!hasAccess) {
                throw new AccessDeniedException("User has no access to this organization");
            }

            OrgContext.setOrgId(orgId);
            filterChain.doFilter(req, res);

        } finally {
            OrgContext.clear();
        }
    }
}
