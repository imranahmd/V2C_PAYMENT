package com.rew.payment.router;

import javax.servlet.http.HttpServletRequest;

public abstract interface IDynamicPGSelector
{
  public abstract String redirectToPG(HttpServletRequest paramHttpServletRequest);
}
