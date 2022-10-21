using Dapper;
using Models;
using Repository.DbContext;
using Repository.Interfaces;
using System;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Dapper.SqlMapper;

namespace Repository
{
    public class UserRepository : IUserRepository
    {
        private readonly DapperContext context;

        public UserRepository(DapperContext _context) => context = _context;

        public async Task<IEnumerable<User>> GetAllAsync()
        {
            using (var connection = context.CreateConnection())
            {
                return connection.Query<User>("User_GetAll", commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<User> GetByIdAsync(int id)
        {

            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<User>("User_GetById", new
                {
                    Id = id
                }, commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<User> AddAsync(User entity)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<User>("User_Add", new
                {
                    Username = entity.Username,
                    Password = entity.Password,
                    FirstName = entity.FirstName,
                    LastName = entity.LastName,
                    CreatedDate = entity.CreatedDate,
                    CreatedBy = entity.CreatedBy,
                }, commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<bool> DeleteAsync(int id)
        {
            using (var connection = context.CreateConnection())
            {
                await connection.ExecuteAsync("User_Delete", new
                {
                    Id = id
                }, commandType: CommandType.StoredProcedure);
            }

            return true;
        }

        public async Task<User> UpdateAsync(User entity)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<User>("User_Update", new
                {
                    Id = entity.Id,
                    Username = entity.Username,
                    Password = entity.Password,
                    FirstName = entity.FirstName,
                    LastName = entity.LastName,
                    ModifiedDate = entity.ModifiedDate,
                    ModifiedBy = entity.ModifiedBy,
                }, commandType: CommandType.StoredProcedure);
            }
        }

        public async Task<int> Login(string username, string password)
        {
            using (var connection = context.CreateConnection())
            {
                return connection.QueryFirstOrDefault<int>("User_Login", new
                {
                    Username = username,
                    Password = password
                }, commandType: CommandType.StoredProcedure);
            }
        }
    }
}
