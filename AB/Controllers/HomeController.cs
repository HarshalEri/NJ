using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.IO;
using System.Net.Mime;
using AB.Models.ViewModels;
using Newtonsoft.Json;
using AB.Models.ModelClasses;

namespace AB.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            DashboardViewModel vm = new DashboardViewModel();
            string path = Server.MapPath(("~/ABFL_RPA/TestJsons/LOB_List.json"));
            
            string jsonText = System.IO.File.ReadAllText(path);
            string[]  AllLOB = JsonConvert.DeserializeObject<string[]>(jsonText);
            vm.LOBList = new List<SelectListItem>();
            foreach (string a in AllLOB)
            {
                vm.LOBList.Add(new SelectListItem { Text = a, Value = a });
            }
            string path1 = Server.MapPath(("~/ABFL_RPA/TestJsons/" + AllLOB.FirstOrDefault() + ".json"));
            //vm.LOBList.Insert(0, new SelectListItem { Text = "Select", Value = "Select" });
            jsonText = System.IO.File.ReadAllText(path1);
            vm.LOBInfo = JsonConvert.DeserializeObject<LOBModel>(jsonText);
            
            return View(vm);
        }

        public ActionResult Login()
        {
            return View();
        }

        public ActionResult GetLogs(string fileName)
        {
            List<string> allLines = System.IO.File.ReadAllLines(Server.MapPath("~/ABFL_RPA/TestReport/" + fileName)).ToList();
            string allText = string.Join("<br/>", allLines);    
            
            return Json(allText);
        }

        public ActionResult ExecuteBatch(string processName)
        {
            string process = processName.Replace(" ", "");
            string path = Server.MapPath(("~/ABFL_RPA"));
            string filePath = path + "\\executeProcess.bat";
            //string txtfilePath = path + "\\a.txt";
            using (StreamWriter sw = new StreamWriter(filePath))
            {
                sw.WriteLine("cd /d " + path);
                sw.WriteLine("ant -DprocessName=" + process);
                //sw.WriteLine("pause");
            }
            System.Diagnostics.Process p = new System.Diagnostics.Process();
            //p.StartInfo.FileName = filePath;
            p.StartInfo.FileName = filePath;
            p.StartInfo.Verb = "runas";
            p.StartInfo.UseShellExecute = false;
            p.StartInfo.RedirectStandardOutput = true;
            p.StartInfo.RedirectStandardError = true;
            p.Start();

            string procOutput = p.StandardOutput.ReadToEnd();
            string procError = p.StandardError.ReadToEnd();

            p.WaitForExit();
            return Json("success");
        }
    }
}