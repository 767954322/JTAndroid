package com.homechart.app.widget.pulltprefresh;

/**
 * @author  luchongbin .
 * @version 1.0 .
 * @date    16-6-13 上午10:14
 * @file    Pullable.java  .
 * @brief    自定义 ListView的上拉刷新，下拉加载更多的接口.
 */
public interface Pullable
{
    /**
     * 判断是否可以下拉，如果不需要下拉功能可以直接return false
     *
     * @return true如果可以下拉否则返回false
     */
    boolean canPullDown();

    /**
     * 判断是否可以上拉，如果不需要上拉功能可以直接return false
     *
     * @return true如果可以上拉否则返回false
     */
    boolean canPullUp();
}
