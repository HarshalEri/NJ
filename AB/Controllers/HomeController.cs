using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.IO;
using System.Net.Mime;

namespace AB.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult GetLogs(string fileName)
        {
            List<string> allLines = System.IO.File.ReadAllLines(Server.MapPath("~/ABFL_RPA/ABFL_RPA/TestReport/" + fileName)).ToList();
            string allText = string.Join("<br/>", allLines);    
            
            return Json(allText);
        }

        public ActionResult ExecuteBatch(string processName)
        {
            string path = Server.MapPath(("~/ABFL_RPA/ABFL_RPA/TestReport"));
            string filePath = path + "\\executeProcess.bat";
            string txtfilePath = path + "\\a.txt";
            using (StreamWriter sw = new StreamWriter(filePath))
            {
                //sw.WriteLine("@echo off");
                sw.WriteLine("echo " + processName + " > " + txtfilePath);
                //sw.WriteLine("pause");
            }
            System.Diagnostics.Process p = new System.Diagnostics.Process();
            p.StartInfo.FileName = filePath;
            //p.StartInfo.UserName = 
            //p.StartInfo.Verb = "runas";
            p.Start();
            p.WaitForExit();
            return Json("success");
        }
    }
}