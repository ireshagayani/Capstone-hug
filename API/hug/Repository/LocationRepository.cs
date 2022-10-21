using Dapper;
using Models;
using Repository.DbContext;
using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Data;
using System.Diagnostics.Metrics;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;

namespace Repository
{
    public class LocationRepository : ILocationRepository
    {
        private readonly DapperContext context;
        public LocationRepository(DapperContext _context) => context = _context;

        public async Task<IEnumerable<Location>> GetAllAsync()
        {
            using (var connection = context.CreateConnection())
            {
                return connection.Query<Location>("Location_GetAll", commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<Location> GetByIdAsync(int id)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<Location>("Location_GetById", new
                {
                    Id = id
                }, commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<Location> AddAsync(Location entity)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<Location>("Location_Add", new
                {
                    Name = entity.Name,
                    Address = entity.Address,
                    City = entity.City,
                    Province = entity.Province,
                    PostalCode = entity.PostalCode,
                    Country = entity.Country,
                    Phone = entity.Phone,
                    LocationTypeId = entity.LocationTypeId,
                    CreatedDate = entity.CreatedDate,
                    CreatedBy = entity.CreatedBy,
                }, commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<bool> DeleteAsync(int id)
        {
            using (var connection = context.CreateConnection())
            {
                await connection.ExecuteAsync("Location_Delete", new
                {
                    Id = id
                }, commandType: CommandType.StoredProcedure);
            }

            return true;
        }
        public async Task<Location> UpdateAsync(Location entity)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<Location>("Update_Add", new
                {
                    Id = entity.Id,
                    Name = entity.Name,
                    Address = entity.Address,
                    City = entity.City,
                    Province = entity.Province,
                    PostalCode = entity.PostalCode,
                    Country = entity.Country,
                    Phone = entity.Phone,
                    LocationTypeId = entity.LocationTypeId,
                    ModifiedDate = entity.ModifiedDate,
                    ModifiedBy = entity.ModifiedBy,
                }, commandType: CommandType.StoredProcedure);
            }
        }
    }
}
