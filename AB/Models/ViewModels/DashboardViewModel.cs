using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using AB.Models.ModelClasses;
using System.Web.Mvc;

namespace AB.Models.ViewModels
{
    public class DashboardViewModel
    {
        public List<SelectListItem> LOBList { get; set; }

        public LOBModel LOBInfo { get; set; }


    }
}