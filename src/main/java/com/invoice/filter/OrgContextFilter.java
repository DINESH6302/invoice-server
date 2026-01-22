package com.invoice.filter;

import com.invoice.context.OrgContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class OrgContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        try {
            String header = req.getHeader("X-Org-Id");

//            if (header == null) {
//                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "X-Org-Id header is missing");
//                return;
//            }
            if (header != null) {
                Long orgId = Long.parseLong(header);
                OrgContext.setOrgId(orgId);
            }

            filterChain.doFilter(req, res);
        } finally {
            OrgContext.clear();
        }
    }
}
