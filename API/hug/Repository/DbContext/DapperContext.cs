using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.Options;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Repository.DbContext
{
    public class DapperContext
    {
        private readonly IConfiguration configuration;
        private readonly string connectionString;

        public DapperContext(IConfiguration _configuration)
        {
            this.configuration = _configuration;
            this.connectionString = configuration.GetConnectionString("SqlConnection");
        }

        public IDbConnection CreateConnection() => new SqlConnection(connectionString);
    }
}
