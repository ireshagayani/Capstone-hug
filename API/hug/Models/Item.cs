using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Models
{
    public class Item : BaseModel
    {
        public string Name { get; set; }
        public string Description { get; set; }
        public string Qty { get; set; }
        public int QtyTypeId {get;set;}
        public string QtyValue { get; set; }
        public int ItemStatusId { get; set; }
    }
}
