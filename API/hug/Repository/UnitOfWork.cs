using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository
{
    public class UnitOfWork : IUnitOfWork
    {
        public UnitOfWork(IUserRepository _userRepository, ILocationRepository _locationRepository, IItemRepository _itemRepository)
        {
            User = _userRepository;
            Location = _locationRepository;
            Item = _itemRepository;
        }
        public IUserRepository User { get; }
        public ILocationRepository Location { get; }
        public IItemRepository Item { get; }
    }
}
