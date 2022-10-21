using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.Interfaces
{
    public interface IUnitOfWork
    {
        IUserRepository User { get; }
        ILocationRepository Location { get; }
        IItemRepository Item { get; }
    }
}
