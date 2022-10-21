using Dapper;
using Models;
using Repository.DbContext;
using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository
{
    public class ItemRepository : IItemRepository
    {
        private readonly DapperContext context;

        public ItemRepository(DapperContext _context) => context = _context;

        public async Task<IEnumerable<Item>> GetAllAsync()
        {
            throw new NotImplementedException();
        }

        public async Task<Item> GetByIdAsync(int id)
        {
            throw new NotImplementedException();
        }
        public async Task<Item> AddAsync(Item entity)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<Item>("Add_Item", new
                {
                    Name = entity.Name,
                    Description = entity.Description,
                    QtyTypeId = entity.QtyTypeId,
                    QtyValue = entity.QtyValue,
                    ItemStatusId = entity.ItemStatusId,
                    CreatedDate = entity.CreatedDate,
                    CreatedBy = entity.CreatedBy,
                }, commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<bool> DeleteAsync(int id)
        {
            using (var connection = context.CreateConnection())
            {
                await connection.ExecuteAsync("Delete_Item", new
                {
                    Id = id
                }, commandType: CommandType.StoredProcedure);
            }

            return true;
        }

        public async Task<Item> UpdateAsync(Item entity)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<Item>("Update_Item", new
                {
                    Id = entity.Id,
                    Name = entity.Name,
                    Description = entity.Description,
                    QtyTypeId = entity.QtyTypeId,
                    QtyValue = entity.QtyValue,
                    ItemStatusId = entity.ItemStatusId,
                    ModifiedDate = entity.ModifiedDate,
                    ModifiedBy = entity.ModifiedBy,
                }, commandType: CommandType.StoredProcedure);
            }
        }
    }
}
