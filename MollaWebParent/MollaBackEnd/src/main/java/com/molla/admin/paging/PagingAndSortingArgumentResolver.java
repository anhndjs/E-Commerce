//package com.molla.admin.paging;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.MethodParameter;
//
//public class PagingAndSortingArgumentResolver {
//    private static final Logger LOGGER = LoggerFactory.getLogger(PagingAndSortingArgumentResolver.class);
//
//    @Override
//    public boolean supportsParameter(MethodParameter parameter) {
//        // TODO Auto-generated method stub
//        return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
//    }
//
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
//                                  NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
//        // TODO Auto-generated method stub
//
//        PagingAndSortingParam annotation = parameter.getParameterAnnotation(PagingAndSortingParam.class);
//        String sortDir = request.getParameter("sortDir");
//        String sortField = request.getParameter("sortField");
//        String keyword = request.getParameter("keyword");
//
//        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortDir", sortDir);
//        model.addAttribute("reverseSortDir", reverseSortDir);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("moduleURL", annotation.moduleURL());
//
//        LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | sortField : " + sortField);
//        LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | sortDir : " + sortDir);
//        LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | reverseSortDir : " + reverseSortDir);
//        LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | keyword : " + keyword);
//        LOGGER.info("PagingAndSortingArgumentResolver | resolveArgument | moduleURL : " + annotation.moduleURL());
//
//        return new PagingAndSortingHelper(model, annotation.listName(),
//                sortField, sortDir, keyword);
//    }
//
//}
