using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace AB.Models.ModelClasses
{
    public class LOBModel
    {
        public Process[] AllProcesses { get; set; }
    }

    public class Process
    {
        public string ProcessName { get; set; }

        public string ExecutionStatus { get; set; }

        public string ExecutionTime { get; set; }
    }
}